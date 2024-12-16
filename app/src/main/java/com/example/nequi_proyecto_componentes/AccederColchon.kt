package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperUpsNoTeAlcanza

class AccederColchon : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.acceder_colchon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCantidad = findViewById<EditText>(R.id.txtCuantoVasAMeter)
        val btnSumar = findViewById<ImageButton>(R.id.btnSumar)
        val btnRestar = findViewById<ImageButton>(R.id.btnRestar)
        val btnListo = findViewById<Button>(R.id.btnListoColchon)
        val btnSacar = findViewById<Button>(R.id.btnSacarColchon)
        val txtCantidadEncolchon = findViewById<TextView>(R.id.txtCantidadEnColchon)
        val btnVolver = findViewById<ImageButton>(R.id.btnVoler)
        val txtDineroEnCholchon = findViewById<TextView>(R.id.txtTotalEnColchon)




        val cuenta = intent.getStringExtra("numeroDeCuenta").toString()


        val bdHelper = DatabaseHelper(this)
        val res = bdHelper.obtenerColchonPorID(cuenta)
        txtDineroEnCholchon.text = if (res != null) "$"+res["Monto"] ?: "" else ""






        // Colores en formato hexadecimal
        val colorBotonHabilitado = 0xFFD80081.toInt() // Rojo habilitado
        val colorBotonDeshabilitado = 0xFFFFFFFF.toInt() // Blanco deshabilitado

        // Desactiva el botón inicialmente
        btnListo.isEnabled = false
        btnListo.setBackgroundColor(colorBotonDeshabilitado)
        btnListo.setTextColor(colorBotonDeshabilitado)
        btnSacar.isEnabled = false
        btnSacar.setBackgroundColor(colorBotonDeshabilitado)
        btnSacar.setTextColor(colorBotonDeshabilitado) // Texto blanco cuando está deshabilitado

        // TextWatcher para controlar el estado del botón btnListo
        txtCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                btnListo.isEnabled = cantidad > 50
                btnSacar.isEnabled = cantidad > 50
                txtCantidadEncolchon.text = "$"+cantidad.toString() // Actualiza txtCantidadEncolchon

                // Cambia el color de fondo y texto según el estado del botón
                if (btnListo.isEnabled) {
                    btnListo.setBackgroundColor(colorBotonHabilitado)
                    btnListo.setTextColor(0xFFFFFFFF.toInt()) // Texto blanco cuando está habilitado
                    btnSacar.setBackgroundColor(colorBotonHabilitado)
                    btnSacar.setTextColor(0xFFFFFFFF.toInt()) // Texto blanco cuando está habilitado
                } else {
                    btnListo.setBackgroundColor(colorBotonDeshabilitado)
                    btnListo.setTextColor(colorBotonDeshabilitado)
                    btnSacar.setBackgroundColor(colorBotonDeshabilitado)
                    btnSacar.setTextColor(colorBotonDeshabilitado)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No se necesita implementar esto
            }
        })

        btnSumar.setOnClickListener {
            val cantidad = txtCantidad.text.toString().toIntOrNull() ?: 0
            txtCantidad.setText((cantidad + 1000).toString())
        }

        btnRestar.setOnClickListener {
            val cantidad = txtCantidad.text.toString().toIntOrNull() ?: 0
            if (cantidad >= 1000) {
                txtCantidad.setText((cantidad - 1000).toString())
            } else {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            }
        }

        btnListo.setOnClickListener {
            // Obtener el saldo disponible de la cuenta como String, luego convierte a Int
            val saldoDisponibleStr = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta)
            val cantidadDisponible = saldoDisponibleStr?.toIntOrNull() // Convierte el saldo a Int o null si no es válido

            // Obtener la cantidad introducida por el usuario y asegúrate de que es Int o 0 si no es válido
            val cantidad = txtCantidad.text.toString().toIntOrNull() ?: 0
            // Compara la cantidad introducida con la cantidad disponible
            if (cantidad <= (cantidadDisponible ?: 0)) {

                bdHelper.actualizarMontoColchonSumar(cuenta.toLong(), cantidad.toLong())
                bdHelper.actualizarSaldoCuenta(cuenta.toLong(), cantidad.toLong()*-1)

                val popupHelper = PopupHelperConfirm(this)
                popupHelper.showConfirmationPopup(btnListo) // Asegúrate de pasar la vista adecuada

                val handler = Handler(Looper.getMainLooper())

                handler.postDelayed({
                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    startActivity(intent)
                }, 100)
            } else {
                val popupHelper = PopupHelperUpsNoTeAlcanza(this)
                popupHelper.showPopup(btnListo)
            }
        }



        btnSacar.setOnClickListener {
            val handler = Handler(Looper.getMainLooper())
            // Obtener el saldo disponible de la cuenta como String, luego convierte a Int
            val saldoDisponibleStr = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta)
            val cantidadDisponible = saldoDisponibleStr?.toIntOrNull() // Convierte el saldo a Int o null si no es válido

            // Obtener la cantidad introducida por el usuario y asegúrate de que es Int o 0 si no es válido
            val cantidad = txtCantidad.text.toString().toInt()
            val cantidadEncolchon = txtDineroEnCholchon.text.toString().drop(1).toInt()


            // Compara la cantidad introducida con la cantidad disponible
            if (cantidad <= cantidadEncolchon) {

                val cantidad2 = cantidad * -1;
                bdHelper.actualizarMontoColchonSumar(cuenta.toLong(), cantidad2.toLong())
                bdHelper.actualizarSaldoCuenta(cuenta.toLong(), cantidad2.toLong()*-1)
                val popupHelper = PopupHelperConfirm(this)
                popupHelper.showConfirmationPopup(btnSacar) // Asegúrate de pasar la vista adecuada


                handler.postDelayed({
                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    startActivity(intent)
                }, 100)

            } else {
                val popupHelper = PopupHelperUpsNoTeAlcanza(this)
                popupHelper.showPopup(btnSacar)

                handler.postDelayed({
                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    startActivity(intent)
                }, 100)
            }
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Sobrescribir el evento de retroceso con el botón hacia atrás del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
