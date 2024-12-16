package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Html.ImageGetter
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: View // View representing the ConstraintLayout
    private lateinit var btnEntrar: Button
    private lateinit var txtNumero: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        mainLayout = findViewById(R.id.main)
        btnEntrar = findViewById(R.id.bntEntrar1)
        txtNumero = findViewById(R.id.txtNumeroTelefono)
        val btnClaveDinamica = findViewById<ImageButton>(R.id.btnClaveDinamica)
        val btnAyudaNequi = findViewById<ImageButton>(R.id.btnAyudaNequi)
        val btnRegistro = findViewById<ImageButton>(R.id.btnRegistro)
        //////////////
        val popupHelperCD = PopupHelperClaveDinamica(this)
        var progreso = 0
        val handler = android.os.Handler(mainLooper)
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
        //////////////////

        btnAyudaNequi.setOnClickListener {
            val popupHelper = PopupHelperAyudaNequi(this)
            popupHelper.showPopup(btnAyudaNequi)
        }


        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupKeyboardListener()

        btnEntrar.setOnClickListener {
            val bdHelper = DatabaseHelper(this)
            val telefono = txtNumero.text.toString().trim()

            if (telefono.isNotEmpty()) {
                try {
                    val existe = bdHelper.ConsultaValidarExistenciaDelaCuentaPorTelefono(telefono)
                    if (existe == 1) {
                        val intent = Intent(this, LoginContrasena::class.java).apply {
                            putExtra("numeroDeCuenta", telefono)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "El número de teléfono no está registrado", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa un número de teléfono", Toast.LENGTH_SHORT).show()
            }
        }
        btnRegistro.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "crear_usuario")
            }
            startActivity(intent)
        }


    }

    private fun setupKeyboardListener() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.rootView.height
                val keypadHeight = screenHeight - rect.bottom

                if (keypadHeight > screenHeight * 0.15) { // Keyboard is visible
                    mainLayout.setPadding(0, 0, 0, keypadHeight)
                } else { // Keyboard is hidden
                    mainLayout.setPadding(0, 0, 0, 0)
                }
            }
        })
    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }

}
