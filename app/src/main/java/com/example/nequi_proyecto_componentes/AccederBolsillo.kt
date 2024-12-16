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

class AccederBolsillo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.acceder_bolsillo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreBolsillo = intent.getStringExtra("nombre_bolsillo_2")
        val cantidadEnBolsillo = intent.getStringExtra("cantidad_en_bolsillo_2")

        val txtNombreBolsillo = findViewById<TextView>(R.id.txtNombreDelBolsilloAcceder)
        val txtCantidadEnBolsillo = findViewById<TextView>(R.id.txtCantidadEnBolsilloAcceder)
        val txtNombreBolsilloEditar = findViewById<EditText>(R.id.txtNombreDelBolsilloAccederEditanble)
        val btnListoEditarBolsillo = findViewById<Button>(R.id.btnListoEditarBolsillo)
        val btnRetirarBolsillo = findViewById<Button>(R.id.btnRetirarBolsillo)
        val txtCuantoVasADejarBolsillo = findViewById<EditText>(R.id.txtCuantoVasADejarBolsillo)
        val btnSumar = findViewById<ImageButton>(R.id.btnSumarBolsillo)
        val btnRestar = findViewById<ImageButton>(R.id.btnRestarBolsillo)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverAccederBolsillo)
        val cuenta = intent.getStringExtra("numeroDeCuenta").toString()
        // Colores en formato hexadecimal
        val colorBotonHabilitado = 0xFFD80081.toInt() // Rojo habilitado
        val colorBotonDeshabilitado = 0xFFFFFFFF.toInt() // Color de fondo blanco deshabilitado
        val colorTextoDeshabilitado = 0xFFFFFFFF.toInt() // Color de texto blanco deshabilitado

        txtNombreBolsillo.text = nombreBolsillo
        txtCantidadEnBolsillo.text = cantidadEnBolsillo
        txtNombreBolsilloEditar.hint = nombreBolsillo

        // Desactiva el botón inicialmente
        btnListoEditarBolsillo.isEnabled = false
        btnListoEditarBolsillo.setBackgroundColor(colorBotonDeshabilitado)
        btnListoEditarBolsillo.setTextColor(colorTextoDeshabilitado)
        btnRetirarBolsillo.isEnabled = false
        btnRetirarBolsillo.setBackgroundColor(colorBotonDeshabilitado)
        btnRetirarBolsillo.setTextColor(colorTextoDeshabilitado)

        // Función para verificar las condiciones
        fun verificarCondiciones() {
            val nombreEditado = txtNombreBolsilloEditar.text.toString()
            val nombreOriginal = txtNombreBolsillo.text.toString()
            val cuantoDejar = if (txtCuantoVasADejarBolsillo.text.isNullOrEmpty()) 0 else txtCuantoVasADejarBolsillo.text.toString().toInt()

            // Condiciones individuales
            val nombreEditadoValido = nombreEditado.isNotEmpty() && nombreEditado.trim() != nombreOriginal
            val cuantoDejarValido = cuantoDejar >= 50

            // Activar el botón si al menos una condición es verdadera
            val condicionesCumplidas = nombreEditadoValido || cuantoDejarValido

            // Cambiar el estado del botón según las condiciones
            btnListoEditarBolsillo.isEnabled = condicionesCumplidas
            btnRetirarBolsillo.isEnabled = condicionesCumplidas

            btnListoEditarBolsillo.setBackgroundColor(
                if (condicionesCumplidas) colorBotonHabilitado else colorBotonDeshabilitado
            )
            btnListoEditarBolsillo.setTextColor(
                if (condicionesCumplidas) colorTextoDeshabilitado else colorTextoDeshabilitado
            )
            btnRetirarBolsillo.setBackgroundColor(
                if (condicionesCumplidas) colorBotonHabilitado else colorBotonDeshabilitado
            )
            btnRetirarBolsillo.setTextColor(
                if (condicionesCumplidas) colorTextoDeshabilitado else colorTextoDeshabilitado
            )
        }

        // TextWatchers para monitorear cambios en los campos de texto
        txtNombreBolsilloEditar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                verificarCondiciones()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        txtCuantoVasADejarBolsillo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                verificarCondiciones()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnSumar.setOnClickListener {
            val valor = (txtCuantoVasADejarBolsillo.text.toString().toIntOrNull() ?: 0) + 1000
            txtCuantoVasADejarBolsillo.setText(valor.toString())
        }

        btnRestar.setOnClickListener {
            val valor = (txtCuantoVasADejarBolsillo.text.toString().toIntOrNull() ?: 0)
            if (valor >= 1000) {
                txtCuantoVasADejarBolsillo.setText((valor - 1000).toString())
            } else if (valor > 0) {
                txtCuantoVasADejarBolsillo.setText("")
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta", intent.getStringExtra("numeroDeCuenta"))
            }
            startActivity(intent)
        }

        btnListoEditarBolsillo.setOnClickListener {
            val bdHelper = DatabaseHelper(this)
            val cantidadDisponibleEnCuenta = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta).toString().toInt()
            val cuantoVasADejar = txtCuantoVasADejarBolsillo.text.toString().toIntOrNull() ?: 0
            val nuevoNombre = txtNombreBolsilloEditar.text.toString()

            if (((txtCuantoVasADejarBolsillo.text.toString().isNotEmpty() && cuantoVasADejar < cantidadDisponibleEnCuenta) ||
                        (txtNombreBolsilloEditar.text.toString().isNotEmpty() && txtNombreBolsilloEditar.text.toString().trim() != txtNombreBolsillo.text.toString()))
                && cuantoVasADejar <= cantidadDisponibleEnCuenta) {

                // Consulta para actualizar los valores del bolsillo
                bdHelper.actualizarBolsillo(nombreBolsillo.toString(), nuevoNombre, cuantoVasADejar.toString())
                 bdHelper.actualizarSaldoCuenta(cuenta.toLong() , cuantoVasADejar.toLong() * -1)

                val popupHelper = PopupHelperConfirm(this)
                popupHelper.showConfirmationPopup(btnListoEditarBolsillo)
                val intent = Intent(this, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", intent.getStringExtra("numeroDeCuenta"))
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(intent)
                }, 100)
            } else {
                val popupHelper = PopupHelperUpsNoTeAlcanza(this)
                popupHelper.showPopup(btnListoEditarBolsillo)
            }
        }
//*********
        btnRetirarBolsillo.setOnClickListener {
            val bdHelper = DatabaseHelper(this)
            val cantidadDisponibleEnCuenta = bdHelper.obtenerSaldoDisponibleCuentaPorID(intent.getStringExtra("numeroDeCuenta").toString()).toString().toInt()
            val cuantoVasADejar = (txtCuantoVasADejarBolsillo.text.toString().toIntOrNull() ?: 0) * -1

            val nuevoNombre = txtNombreBolsilloEditar.text.toString()

            if (((txtCuantoVasADejarBolsillo.text.toString().isNotEmpty() && cuantoVasADejar < cantidadDisponibleEnCuenta) ||
                        (txtNombreBolsilloEditar.text.toString().isNotEmpty() && txtNombreBolsilloEditar.text.toString().trim() != txtNombreBolsillo.text.toString()))
                && cuantoVasADejar <= cantidadDisponibleEnCuenta) {

                // Consulta para actualizar los valores del bolsillo
                bdHelper.actualizarBolsillo(nombreBolsillo.toString(), nuevoNombre, cuantoVasADejar.toString())
                 bdHelper.actualizarSaldoCuenta(cuenta.toLong(), cuantoVasADejar.toLong() * -1)

                val popupHelper = PopupHelperConfirm(this)
                popupHelper.showConfirmationPopup(btnListoEditarBolsillo)
                val intent = Intent(this, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", intent.getStringExtra("numeroDeCuenta"))
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(intent)
                }, 100)
            } else {
                val popupHelper = PopupHelperUpsNoTeAlcanza(this)
                popupHelper.showPopup(btnListoEditarBolsillo)
            }
        }
    }

    override fun onBackPressed() {
        // Sobrescribir el evento de retroceso con el botón hacia atrás del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
