package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperTusBolsillosParte1
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperTusMetasParte1
import java.io.FileNotFoundException

class MenuPrincipal : AppCompatActivity() {


    private lateinit var layoutFavoritos: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        // Configuración de WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //intento de llamar el popup "tus_favoritos"


        val imageButton: ImageButton = findViewById(R.id.btnPopup)
        val btnTuPlata = findViewById<Button>(R.id.btn_tu_plata)
        val btnMovimientos = findViewById<ImageButton>(R.id.btnMovimientos)
        val btnServvicios = findViewById<ImageButton>(R.id.btnServicios)
        val favoritos  = findViewById<ImageButton>(R.id.btnFavoritos)
        val btnClaveDinamica = findViewById<ImageButton>(R.id.btnClaveDinamica)
        val btnAyudaNequi = findViewById<ImageButton>(R.id.btnAyudaNequi)
        val btnNotificacion = findViewById<ImageButton>(R.id.btnNotificacion)
        val btnPerfil = findViewById<ImageButton>(R.id.btnperfil)
        val txtDineroExterno = findViewById<TextView>(R.id.txtDineroExterno)
        val txtDineroTotal = findViewById<TextView>(R.id.txtDineroTotal)
        val txtNombreUsuario = findViewById<TextView>(R.id.txtNombreUsuario)
        val btnMostrarDepositoBajoMonto = findViewById<ImageButton>(R.id.btnMostrarDepositoBajoMonto)

        val cuenta = intent.getStringExtra("numeroDeCuenta").toString()
//        Toast.makeText(this,"Valor recivido " + cuenta, Toast.LENGTH_SHORT).show()
        val bdHelper = DatabaseHelper(this)
        val res = bdHelper.obtenerSaldoYNombrePorID(cuenta)
        val res2 = bdHelper.obtenerTotalPorID(cuenta)
        txtDineroExterno.text = "$"+ res?.get("Saldo") ?: cuenta
        txtNombreUsuario.text = res?.get("Nombre") ?: "Usuario"
        txtDineroTotal.text = res2.toString()

        layoutFavoritos = findViewById(R.id.layoutFavoritos)
        loadPopup(cuenta)


        // Configurar el PopupWindow para el ImageButton

        imageButton.setOnClickListener {

            val popupHelper = PopupButtonWindowHelper(this)
            popupHelper.showPopup(imageButton , cuenta)
        }
        btnTuPlata.setOnClickListener{
            val popupHelper = PopupHelperTuPlataDispoColchBolsi(this)
            popupHelper.showPopup(btnTuPlata , cuenta)
        }
        btnMovimientos.setOnClickListener{
            val popupHelper = PopupHelperMovimientos(this)
            popupHelper.showPopup(btnMovimientos, cuenta)
        }
        btnServvicios.setOnClickListener{
            val popupHelper = PopupHelperServiciosNequi(this)
            popupHelper.showPopup(btnServvicios)
        }
        btnAyudaNequi.setOnClickListener{
            val popupHelper = PopupHelperAyudaNequi(this)
            popupHelper.showPopup(btnAyudaNequi)
        }
        btnNotificacion.setOnClickListener{
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "ver_notificaciones")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
        btnPerfil.setOnClickListener{
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "ver_perfil")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
        btnMostrarDepositoBajoMonto.setOnClickListener{
            if(txtDineroExterno.text.toString().drop(1).toDoubleOrNull() == null){
                txtDineroExterno.text = "$"+ res?.get("Saldo") ?: cuenta
                txtDineroTotal.text = "$"+ res2.toString() ?: cuenta
            }else{
                txtDineroExterno.setText("********")
                txtDineroTotal.setText("********")
            }
        }


        val popupHelperCD = PopupHelperClaveDinamica(this)
        var progreso = 0
        val handler = Handler(mainLooper)
        val runnable = object : Runnable {
            override fun run() {

                popupHelperCD.updateProgressBar(progreso)
                if (progreso >= 60) {
                    progreso = 0
                }
                handler.postDelayed(this, 1000) // Actualizar cada 1 segundo
                progreso += 1
            }
        }

        // Iniciar la actualización del progreso
        handler.post(runnable)
        btnClaveDinamica.setOnClickListener {
            popupHelperCD.showPopup(btnClaveDinamica)
        }

        loadImage(btnPerfil)





    }




    private fun loadImage(imageButton: ImageButton) {
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val uriString = sharedPreferences.getString("imageUri", null)
        if (uriString != null) {
            val uri = Uri.parse(uriString)
            try {
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 110, 120, false)
                imageButton.background = null
                imageButton.setImageBitmap(resizedBitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                // Si no se puede cargar la imagen, puedes manejar el error aquí
                Toast.makeText(this, "No se pudo cargar la imagen de perfil.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }

    private fun loadPopup(cuenta : String) {
        // Infla el layout del popup
        val inflater = LayoutInflater.from(this)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_tus_favoritos_pagina_principal, layoutFavoritos, false)

        // Agregar el popup al contenedor
        layoutFavoritos.addView(popupView)

        // Configura los eventos del popup
        val btnColchonFavoritos = popupView.findViewById<ImageButton>(R.id.btnColchonFavoritos)
        val btnBolsilloFavoritos = popupView.findViewById<ImageButton>(R.id.btnBolsilloFavoritos)
        val btnMetasFavoritos = popupView.findViewById<ImageButton>(R.id.btnMetasFavoritos)

        btnColchonFavoritos.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina" , "acceder_colchon")
                putExtra("numeroDeCuenta" , cuenta)
            }
            this.startActivity(intent)
        }

        btnBolsilloFavoritos.setOnClickListener {
            val popupHelper = PopupHelperTusBolsillosParte1(this)
            popupHelper.showPopup(btnBolsilloFavoritos , cuenta)
        }

        btnMetasFavoritos.setOnClickListener {
            val popupHelper = PopupHelperTusMetasParte1(this , cuenta)
            popupHelper.showPopup(btnMetasFavoritos , cuenta)
        }

    }





}
