package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper

class LoginContrasena : AppCompatActivity() {

    private lateinit var passwordField1: EditText
    private lateinit var passwordField2: EditText
    private lateinit var passwordField3: EditText
    private lateinit var passwordField4: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_contrasena)

        // Configura el listener para los insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Encuentra los campos de texto y el botón
        passwordField1 = findViewById(R.id.editTextNumberPassword1)
        passwordField2 = findViewById(R.id.editTextNumberPassword2)
        passwordField3 = findViewById(R.id.editTextNumberPassword3)
        passwordField4 = findViewById(R.id.editTextNumberPassword4)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver1)

        passwordField1.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        // Configura el TextWatcher para cambiar el enfoque automáticamente
        setupPasswordFieldFocus(passwordField1, passwordField2)
        setupPasswordFieldFocus(passwordField2, passwordField3)
        setupPasswordFieldFocus(passwordField3, passwordField4)
        setupPasswordFieldFocus(passwordField4, null) // Último campo, no hay siguiente

        // Configura el botón para regresar a la actividad principal
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Configura los TextWatchers para verificar el estado de los campos
        setupFieldWatcher(passwordField1)
        setupFieldWatcher(passwordField2)
        setupFieldWatcher(passwordField3)
        setupFieldWatcher(passwordField4)
    }

    // Función para configurar el cambio de enfoque
    private fun setupPasswordFieldFocus(currentField: EditText, nextField: EditText?) {
        currentField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    nextField?.requestFocus() // Mueve el enfoque al siguiente campo
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Si se borra el texto, vuelve al campo anterior
                if (before == 1 && count == 0) {
                    when (currentField) {
                        passwordField2 -> passwordField1.requestFocus()
                        passwordField3 -> passwordField2.requestFocus()
                        passwordField4 -> passwordField3.requestFocus()
                    }
                }
            }
        })
    }

    // Configura TextWatcher para cada campo
    private fun setupFieldWatcher(field: EditText) {
        field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkAllFields() // Verifica los campos cada vez que uno cambia
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Función para verificar si todos los campos están llenos
    private fun checkAllFields() {
        val campo1 = passwordField1.text.toString()
        val campo2 = passwordField2.text.toString()
        val campo3 = passwordField3.text.toString()
        val campo4 = passwordField4.text.toString()

        if (campo1.isNotEmpty() && campo2.isNotEmpty() && campo3.isNotEmpty() && campo4.isNotEmpty()) {
            val bdHelper = DatabaseHelper(this)
            val cuenta = intent.getStringExtra("numeroDeCuenta").toString()
            val contra = passwordField1.text.toString() + passwordField2.text.toString() + passwordField3.text.toString() + passwordField4.text.toString()

            if (contra.equals(bdHelper.obtenerContrasenaPorID(cuenta))){
                val intent = Intent(this, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta" , cuenta)
                }
                startActivity(intent)
            }else{
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }




        }
    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
