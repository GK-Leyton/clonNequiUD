package com.example.nequi_proyecto_componentes

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperCancel
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirmAskForMoneyTransfiya
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PedirPlata : AppCompatActivity() {

    private lateinit var btnPedirpla: Button
    private lateinit var numeroPersona: EditText
    private lateinit var cantidadPedir: EditText
    private lateinit var mensajePedir: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedir_plata)

        // Encuentra las vistas
        btnPedirpla = findViewById(R.id.btn_pedirPlata)
        numeroPersona = findViewById(R.id.txtNumeroTelefonoPedir)
        cantidadPedir = findViewById(R.id.txtCantidadPedir)
        mensajePedir = findViewById(R.id.txtMensajePedir)

        val btnAtras = findViewById<ImageButton>(R.id.btnVolver_pedirPlata)


        val mensaje = intent.getStringExtra("mensaje2")
        val cuenta = intent.getStringExtra("numeroDeCuenta")
        val bdHelper = DatabaseHelper(this)

        //Toast.makeText(this, "Mensaje recibido en PedirPlata:" + cuenta, Toast.LENGTH_SHORT).show()

        btnAtras.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }

        // Configura el clic del botón para mostrar el popup



        if (mensaje == "Pide Plata") {
            btnPedirpla.setOnClickListener {
                hideKeyboard()
                val cuentaExiste = bdHelper.ConsultaValidarExistenciaDelaCuentaPorTelefono(numeroPersona.text.toString())
                val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta.toString())
                if((cuentaExiste == 1) && (!numeroPersona.text.toString().equals(cuenta)) && (cantidadPedir.text.toString().toLong() < 2000000)){
                    //Toast.makeText(this, "Cuenta encontrada", Toast.LENGTH_SHORT).show()
                    val DatosParaPopupHelperConfirm = bdHelper.obtenerNombreUsuario_Telefono(numeroPersona.text.toString())
                    if(DatosParaPopupHelperConfirm != null){
                        //Toast.makeText(this, "Datos para Popup", Toast.LENGTH_SHORT).show()
                        val tipo = "peticion"
                        val mensaje = mensajePedir.text.toString()
                        val fechaActual: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
                        val idQuienPide = bdHelper.obtenerUsuarioIdPorCuentaId(cuenta.toString()).toString()
                        val idAQuienSeLePide = bdHelper.obtenerUsuarioIdPorCuentaId(numeroPersona.text.toString()).toString()
                        val monto = cantidadPedir.text.toString().toString()
                        val estado = "1"

                        bdHelper.insertarMensaje(tipo , mensaje , fechaActual , idAQuienSeLePide , idQuienPide , monto , estado)

                        val popupHelper = PopupHelperConfirm(this)
                        popupHelper.showConfirmationPopup(btnPedirpla)

                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menu_principal")
                            putExtra("numeroDeCuenta" , cuenta)
                        }
                        val handler = android.os.Handler()
                        handler.postDelayed({
                            startActivity(intent)
                        }, 100)
                    }else{
                        Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                        val popupHelper = PopupHelperCancel(this)
                        popupHelper.showConfirmationPopup(btnPedirpla)

                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menu_principal")
                            putExtra("numeroDeCuenta" , cuenta)
                        }
                        val handler = android.os.Handler()
                        handler.postDelayed({
                            startActivity(intent)
                        }, 100)
                    }

                }else{
                    val popupHelper = PopupHelperCancel(this)
                    popupHelper.showConfirmationPopup(btnPedirpla)

                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta" , cuenta)
                    }
                    val handler = android.os.Handler()
                    handler.postDelayed({
                        startActivity(intent)
                    }, 100)

                }


            }
        } else if (mensaje == "Pide Plata por Transfiya") {
            btnPedirpla.setOnClickListener {
                hideKeyboard()
                val cuentaExiste = bdHelper.ConsultaValidarExistenciaDelaCuentaPorTelefono(numeroPersona.text.toString())
                val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta.toString())
                if((cuentaExiste == 1) && (!numeroPersona.text.toString().equals(cuenta)) && (cantidadPedir.text.toString().toLong() < 2000000) && (cantidadPedir.text.toString().toLong() < cantidadDisponible.toString().toLong()) ){
                    val DatosParaPopupHelperConfirm = bdHelper.obtenerNombreUsuario_Telefono(numeroPersona.text.toString())
                    if(DatosParaPopupHelperConfirm != null){


                        val tipo = "peticion"
                        val mensaje = mensajePedir.text.toString()
                        val fechaActual: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
                        val idQuienPide = bdHelper.obtenerUsuarioIdPorCuentaId(cuenta.toString()).toString()
                        val idAQuienSeLePide = bdHelper.obtenerUsuarioIdPorCuentaId(numeroPersona.text.toString()).toString()
                        val monto = cantidadPedir.text.toString().toString()
                        val estado = "1"

                        bdHelper.insertarMensaje(tipo , mensaje , fechaActual , idAQuienSeLePide , idQuienPide , monto , estado)



                        val popupHelper = PopupHelperConfirm(this)
                        popupHelper.showConfirmationPopup(btnPedirpla)

                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menu_principal")
                            putExtra("numeroDeCuenta" , cuenta)
                        }
                        val handler = android.os.Handler()
                        handler.postDelayed({
                            startActivity(intent)
                        }, 100)
                    }else{
                        Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                        val popupHelper = PopupHelperCancel(this)
                        popupHelper.showConfirmationPopup(btnPedirpla)

                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menu_principal")
                            putExtra("numeroDeCuenta" , cuenta)
                        }
                        val handler = android.os.Handler()
                        handler.postDelayed({
                            startActivity(intent)
                        }, 100)
                    }

                }else{
                    val popupHelper = PopupHelperCancel(this)
                    popupHelper.showConfirmationPopup(btnPedirpla)

                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta" , cuenta)
                    }
                    val handler = android.os.Handler()
                    handler.postDelayed({
                        startActivity(intent)
                    }, 100)
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
        cantidadPedir.addTextChangedListener(textWatcher)
        mensajePedir.addTextChangedListener(textWatcher)

    }

    private fun updateButtonState() {
        // Obtiene los valores de los campos
        val numeroPersonaText = numeroPersona.text.toString()
        val cantidadPedirText = cantidadPedir.text.toString()

        // Verifica las condiciones
        val isValidNumeroPersona = numeroPersonaText.length == 10
        val isValidMensaje = mensajePedir.text.length > 5
        val isValidCantidadPedir = try {
            // Convierte el texto a número y verifica si es mayor o igual a 50
            cantidadPedirText.toInt() >= 50
        } catch (e: NumberFormatException) {
            // Si ocurre una excepción, el texto no es un número válido
            false
        }

        // Actualiza el estado del botón
        btnPedirpla.isEnabled = isValidNumeroPersona && isValidCantidadPedir && isValidMensaje
        val colorStateList = ColorStateList.valueOf(
            if (btnPedirpla.isEnabled) Color.parseColor("#d80081") else Color.parseColor("#C74E96")
        )
        btnPedirpla.backgroundTintList = colorStateList
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        // Sobrescribir el evento de retroceso con el botón hacia atrás del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
