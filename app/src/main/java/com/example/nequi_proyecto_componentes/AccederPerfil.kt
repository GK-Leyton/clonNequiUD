package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html.ImageGetter
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperDocumentosYCertificados
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AccederPerfil : AppCompatActivity() {

    private lateinit var photoUri: Uri
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)

        val btnAyuda = findViewById<ImageButton>(R.id.btnAyuda)
        val frameAyuda = findViewById<FrameLayout>(R.id.layout_ayuda)
        val btnSalir = findViewById<ImageButton>(R.id.btnSalir)
        val btnAjustes = findViewById<ImageButton>(R.id.btn_ajustes)
        val frameAjustes = findViewById<FrameLayout>(R.id.layout_ajustes)
        val btnAtras = findViewById<ImageButton>(R.id.btn_atras_perfil)
        val btnDocumentos = findViewById<ImageButton>(R.id.btnDocumentos)
        val frameDocumentos = findViewById<FrameLayout>(R.id.layout_documentos)
        val txtNombreUsuario = findViewById<TextView>(R.id.txtNOmbreUsuario)
        val txtNumeroDeCuenta = findViewById<TextView>(R.id.txtNumeroDeCuentaNequi)
        val cuenta = intent.getStringExtra("numeroDeCuenta").toString()

        val bdHelper = DatabaseHelper(this)
        val res = bdHelper.obtenerSaldoYNombrePorID(cuenta)

        txtNombreUsuario.text = res?.get("Nombre") ?: "Usuario"
        txtNumeroDeCuenta.text = cuenta


        btnAyuda.setOnClickListener {
            val popupHelper = PopupHelperAyudaNequi(this)
            popupHelper.showPopup(btnAyuda)
        }
        frameAyuda.setOnClickListener {
            val popupHelper = PopupHelperAyudaNequi(this)
            popupHelper.showPopup(frameAyuda)
        }
        btnSalir.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "salir")
            }
            startActivity(intent)
        }
        btnAjustes.setOnClickListener {
            val dialog = DialogConfirmacionContraseña.newInstance(cuenta)
            dialog.show(supportFragmentManager, "Dialog Contraseña")

        }
        frameAjustes.setOnClickListener{
            val dialog = DialogConfirmacionContraseña.newInstance(cuenta)
            dialog.show(supportFragmentManager, "Dialog Contraseña")
        }
        btnAtras.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina" , "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
        frameDocumentos.setOnClickListener {
            val popupHelper = PopupHelperDocumentosYCertificados(this)
            popupHelper.showPopup(frameDocumentos)
        }
        btnDocumentos.setOnClickListener {
            val popupHelper = PopupHelperDocumentosYCertificados(this)
            popupHelper.showPopup(btnDocumentos)
        }



        val btnFotoPerfilTomarFotoPerfil = findViewById<ImageButton>(R.id.btnDesplegarCamara_btnFotoPerfil)
        btnFotoPerfilTomarFotoPerfil.setOnClickListener {
            dispatchTakePictureIntent()
        }

        // Cargar la imagen guardada si existe
        loadImage()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(
                this,
                "com.example.nequi_proyecto_componentes.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(null) ?: File("")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageView = findViewById<ImageButton>(R.id.btnDesplegarCamara_btnFotoPerfil)
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(photoUri))
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 110, 140, false)
            imageView.background = null
            imageView.setImageBitmap(resizedBitmap)

            // Guardar el URI de la imagen para cargarla después
            saveImageUri(photoUri)
        }
    }

    private fun saveImageUri(uri: Uri) {
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("imageUri", uri.toString())
            apply()
        }
    }

    private fun loadImage() {
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val uriString = sharedPreferences.getString("imageUri", null)
        if (uriString != null) {
            val uri = Uri.parse(uriString)
            val imageView = findViewById<ImageButton>(R.id.btnDesplegarCamara_btnFotoPerfil)
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 110, 120, false)
            imageView.background = null
            imageView.setImageBitmap(resizedBitmap)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
