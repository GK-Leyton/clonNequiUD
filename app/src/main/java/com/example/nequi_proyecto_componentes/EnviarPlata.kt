package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirmSendMoneyNequi
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirmSendMoneyTransfiyaParte2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EnviarPlata : AppCompatActivity() {

    private lateinit var btnEnviarPlata: Button
    private lateinit var numeroPersona: EditText
    private lateinit var cantidadEnviar: EditText
    private lateinit var txtMensaje : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.enviar_plata)
        // Encuentra las vistas
        btnEnviarPlata = findViewById(R.id.btn_enviarPlata)
        numeroPersona = findViewById(R.id.txtNumeroTelefonoEnviar)
        cantidadEnviar = findViewById(R.id.txtCantidadEnviar)
        txtMensaje = findViewById(R.id.txtMensajeEnviar)
        val txtTitulo = findViewById<TextView>(R.id.txtMensajeEnviarPlata)
        val txtMensajeTransfiya = findViewById<TextView>(R.id.txtMensajeTrasfiyaEnviarPlata)
        val frameDisponible = findViewById<FrameLayout>(R.id.frameDisponible)
        val btnDisponible = findViewById<ImageButton>(R.id.btnDisponible)
        val btnVoler = findViewById<ImageButton>(R.id.btnVolver_enviaPlata)
        val cuenta = intent.getStringExtra("numeroDeCuenta").toString()
        btnVoler.setOnClickListener{
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
        // Configura el clic del botón para mostrar el popup
        val mensaje = intent.getStringExtra("mensaje2")

        if(mensaje.equals("Envía plata") ){
            txtMensajeTransfiya.visibility = View.INVISIBLE
            frameDisponible.visibility = View.INVISIBLE
            btnDisponible.visibility = View.INVISIBLE
        }


        txtTitulo.text = mensaje;

        //importando la conexion con la base de datos
        val bdHelper = DatabaseHelper(this)



        if(mensaje.equals("Envía plata")){
            btnEnviarPlata.setOnClickListener {
                val enviar = cantidadEnviar.text.toString().toInt()
                val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta).toString().toInt()
                val numeroCuentaEnviar = numeroPersona.text.toString()
                val cuentaExiste = bdHelper.ConsultaValidarExistenciaDelaCuentaPorTelefono(numeroCuentaEnviar)
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val mensaje = txtMensaje.text.toString()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if(cuentaExiste == 1 &&  (numeroCuentaEnviar != cuenta)){

                    if(cantidadDisponible >= enviar){

                        val popupHelper = PopupHelperConfirmSendMoneyNequi(this)
                        popupHelper.showPopup(btnEnviarPlata , numeroPersona.text.toString() , cantidadEnviar.text.toString() , numeroCuentaEnviar , enviar.toString() , mensaje , fechaActual , cuenta)
                }
                    else{
                        Toast.makeText(this, "Cantidad Erronea", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "El número de teléfono Erroneo", Toast.LENGTH_SHORT).show()
                }














            }
        }else if(mensaje.equals("Envía plata Transfiya")){
            btnEnviarPlata.setOnClickListener {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)



                val enviar = cantidadEnviar.text.toString().toInt()
                val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta).toString().toInt()
                val numeroCuentaEnviar = numeroPersona.text.toString()
                val cuentaExiste = bdHelper.ConsultaValidarExistenciaDelaCuentaPorTelefono(numeroCuentaEnviar)
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val mensaje = txtMensaje.text.toString()

                if(cuentaExiste == 1 &&  (numeroCuentaEnviar != cuenta)){
                if(cantidadDisponible >= enviar){
                    val popupHelper = PopupHelperConfirmSendMoneyTransfiyaParte2(this)
                    popupHelper.showPopup(btnEnviarPlata , numeroPersona.text.toString() , cantidadEnviar.text.toString() , numeroCuentaEnviar  , enviar.toString() , mensaje , fechaActual , cuenta)
                }
                else{
                    Toast.makeText(this, "Cantidad Erronea", Toast.LENGTH_SHORT).show()
                }
                }
                else{
                    Toast.makeText(this, "El número de teléfono Erronea", Toast.LENGTH_SHORT).show()
                }


            }
        }





        // Configura el TextWatcher para los EditText
        setupTextWatchers()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        numeroPersona.addTextChangedListener(textWatcher)
        cantidadEnviar.addTextChangedListener(textWatcher)
        txtMensaje.addTextChangedListener(textWatcher)
    }

    private fun updateButtonState() {
        // Obtiene los valores de los campos
        val numeroPersonaText = numeroPersona.text.toString()
        val cantidadEnviarText = cantidadEnviar.text.toString()
        val txtMensaje = txtMensaje.text.toString()

        // Verifica las condiciones
        val isValidNumeroPersona = numeroPersonaText.length == 10
        val isValidtxtMensaje = txtMensaje.length > 4
        val isValidCantidadEnviar = try {
            // Convierte el texto a número y verifica si es mayor o igual a 50
            cantidadEnviarText.toInt() >= 50
        } catch (e: NumberFormatException) {
            // Si ocurre una excepción, el texto no es un número válido
            false
        }

        // Actualiza el estado del botón
        btnEnviarPlata.isEnabled = isValidNumeroPersona && isValidCantidadEnviar && isValidtxtMensaje
        val colorStateList = ColorStateList.valueOf(
            if (btnEnviarPlata.isEnabled) Color.parseColor("#d80081") else Color.parseColor("#C74E96")
        )
        btnEnviarPlata.backgroundTintList = colorStateList
    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }

}