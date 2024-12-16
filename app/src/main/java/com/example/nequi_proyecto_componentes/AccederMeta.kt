package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirmarEliminarMeta
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperUpsNoTeAlcanza

class AccederMeta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acceder_meta)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los componentes
        val nombreDeMeta = findViewById<TextView>(R.id.txtNombreDeMeta)
        val fechaInicioMeta = findViewById<TextView>(R.id.txtFechaInicioMeta)
        val cantidadEnMeta = findViewById<TextView>(R.id.txtCantidadEnMeta)
        val cantidadFinalMeta = findViewById<TextView>(R.id.txtCantidadFinalMeta)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        val btnVolverAccederMeta = findViewById<ImageButton>(R.id.btnVolverAccederMeta)
        val btnEliminarMeta = findViewById<ImageButton>(R.id.btnEliminarMeta)
        val nombre_meta_recibido = intent.getStringExtra("nombre_meta_2").toString()
        val cantidad_en_meta_recibido = intent.getStringExtra("cantidad_en_meta_2").toString()
        val cantidad_final_meta_recibido = intent.getStringExtra("cantidad_final_meta_2").toString()
        val fecha_inicio_meta_recibido = intent.getStringExtra("fecha_inicio_meta_2").toString()
        val btnListoMeta = findViewById<Button>(R.id.btnListoMeta)
        val btnRetirar = findViewById<Button>(R.id.btnRetirarMeta)
        val RecargaTuMeta = findViewById<EditText>(R.id.txtRecargaTuMeta)


        val cuenta = intent.getStringExtra("numeroDeCuenta").toString()
        val bdHelper = DatabaseHelper(this)


        nombreDeMeta.text = nombre_meta_recibido
        fechaInicioMeta.text = fecha_inicio_meta_recibido
        cantidadEnMeta.text = "$"+cantidad_en_meta_recibido
        cantidadFinalMeta.text = "$"+cantidad_final_meta_recibido
        val progreso = if (!cantidad_en_meta_recibido.isNullOrEmpty() &&
            !cantidad_final_meta_recibido.isNullOrEmpty()) {
            val cantidadEnMeta = cantidad_en_meta_recibido.toFloatOrNull()
            val cantidadFinalMeta = cantidad_final_meta_recibido.toFloatOrNull()

            if (cantidadEnMeta != null && cantidadFinalMeta != null && cantidadFinalMeta != 0f) {
                ((cantidadEnMeta / cantidadFinalMeta) * 100).toInt()
            } else {
                0 // Valor por defecto en caso de error
            }
        } else {
            0 // Valor por defecto si los valores recibidos son nulos o vacíos
        }

        progressBar.progress = progreso

        if(cantidad_en_meta_recibido.toFloat()  == cantidad_final_meta_recibido.toFloat()){
            Toast.makeText(this, "Meta Completada!!!!!!!!", Toast.LENGTH_SHORT).show()
        }

        btnVolverAccederMeta.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }

        btnEliminarMeta.setOnClickListener {
            val popupHelper = PopupHelperConfirmarEliminarMeta(this)
            popupHelper.showPopup(btnEliminarMeta , cuenta , nombre_meta_recibido , cantidad_en_meta_recibido)
        }

        btnListoMeta.setOnClickListener {
            val totalEnCuenta = bdHelper.obtenerTotalPorID(cuenta)
            var valor = RecargaTuMeta.text.toString().toFloat()  // Usa Float en lugar de Int
            // Convierte a Float para las comparaciones
            if (valor <= totalEnCuenta.toString().toFloat() &&
                (cantidad_en_meta_recibido.toFloat() + valor) <= cantidad_final_meta_recibido.toFloat()) {



                // Actualiza el monto de la meta con valor convertido a Long
                bdHelper.actualizarMontoMeta(nombre_meta_recibido, valor.toLong())
               valor = valor * -1
                bdHelper.actualizarSaldoCuenta(cuenta.toLong(), valor.toLong())
                // Muestra el popup de confirmación
                if(cantidad_en_meta_recibido.toFloat() + valor*-1 == cantidad_final_meta_recibido.toFloat()){
                    Toast.makeText(this, "Meta Completada!!!!!!!!", Toast.LENGTH_SHORT).show()

                    val popupHelper = PopupHelperMetaFinalizada(this)
                    popupHelper.showPopup(btnListoMeta , cuenta , nombre_meta_recibido)
                }else{
                    val popupHelper = PopupHelperConfirm(this)
                    popupHelper.showConfirmationPopup(btnListoMeta)
                    Thread {
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menu_principal")
                            putExtra("numeroDeCuenta", cuenta)
                        }
                        Thread.sleep(500)
                        startActivity(intent)
                    }.start()
                }




                // Crea un hilo para redirigir a la siguiente pantalla

            } else {
                // Muestra el popup en caso de que no se pueda completar la acción
                val popupHelper = PopupHelperUpsNoTeAlcanza(this)
                popupHelper.showPopup(btnVolverAccederMeta)
            }
        }

        //********


        btnRetirar.setOnClickListener {
            var valor = RecargaTuMeta.text.toString().toFloat()  // Usa Float en lugar de Int

            // Convierte a Float para las comparaciones
            if (valor <= cantidad_en_meta_recibido.toFloat()) {

                bdHelper.actualizarSaldoCuenta(cuenta.toLong(), valor.toLong())
                valor = valor * -1
                bdHelper.actualizarMontoMeta(nombre_meta_recibido, valor.toLong())


                // Muestra el popup de confirmación
                val popupHelper = PopupHelperConfirm(this)
                popupHelper.showConfirmationPopup(btnListoMeta)

                // Crea un hilo para redirigir a la siguiente pantalla
                Thread {
                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    Thread.sleep(500)
                    startActivity(intent)
                }.start()
            } else {
                // Muestra el popup en caso de que no se pueda completar la acción
                val popupHelper = PopupHelperUpsNoTeAlcanza(this)
                popupHelper.showPopup(btnVolverAccederMeta)
            }
        }




        // Configura el botón para que esté desactivado por defecto
        btnListoMeta.isEnabled = false
        btnRetirar.isEnabled = false

        // Configura el TextWatcher en el EditText
        RecargaTuMeta.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Verifica si el texto es un número y si es mayor a 50
                val text = s.toString()
                val value = text.toIntOrNull() ?: 0
                btnListoMeta.isEnabled = value > 50
                btnRetirar.isEnabled = value > 50
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    override fun onBackPressed() {
        // Sobrescribir el evento de retroceso con el botón hacia atrás del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
