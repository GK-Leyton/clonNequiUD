package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm

class CreacionUsuario : AppCompatActivity() {
    private lateinit var btnListo: Button
    private lateinit var nombreEditText: EditText
    private lateinit var ocupacionEditText: EditText
    private lateinit var numeroEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var ciudadesSpinner: Spinner
    private var isSpinnerValid: Boolean = false // Variable para controlar el estado del Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creacion_usuario)

        // Inicializar los campos
        nombreEditText = findViewById(R.id.txtNombre_crearUsuario)
        ocupacionEditText = findViewById(R.id.txtOcupacion_crearUsuario)
        numeroEditText = findViewById(R.id.txtNumero_crearUsuario)
        contrasenaEditText = findViewById(R.id.txtContrasena_crearUsuario)
        ciudadesSpinner = findViewById(R.id.spinnerCiudades)
        btnListo = findViewById(R.id.btnListoCrearUsuario)
        val btnAtras = findViewById<ImageButton>(R.id.btnAtras_crearUsuario)
        // Deshabilitar el botón inicialmente
        btnListo.isEnabled = false

        // Configurar el spinner con las ciudades
        val ciudades = listOf(
            "", "Medellin", "Rionegro", "Envigado", "Bello",
            "Bogota", "Soacha", "Chía", "Zipaquira",
            "Ciudad de Panama", "Colón", "San Miguelito", "La Chorrera",
            "Penonome", "Aguadulce", "Nata", "La Pintada"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ciudades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ciudadesSpinner.adapter = adapter

        // Añadir listeners para la validación de los campos
        nombreEditText.addTextChangedListener(textWatcher)
        ocupacionEditText.addTextChangedListener(textWatcher)
        numeroEditText.addTextChangedListener(textWatcher)
        contrasenaEditText.addTextChangedListener(textWatcher)

        // Listener para el Spinner
        ciudadesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                // Validar si se ha seleccionado una ciudad (que no sea el valor vacío)
                isSpinnerValid = position != 0
                btnListo.isEnabled = validateFields() // Llamar a la validación completa
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                isSpinnerValid = false
                btnListo.isEnabled = false
            }
        }

        btnListo.setOnClickListener {
            val ciudadSeleccionada = ciudadesSpinner.selectedItem.toString()
            val idCiudad = valorCiudad(ciudadSeleccionada)
            val nombre = nombreEditText.text.toString()
            val ocupacion = ocupacionEditText.text.toString()
            val numero = numeroEditText.text.toString()
            val fechaActual = java.time.LocalDate.now().toString()
            val bdHelper = DatabaseHelper(this)
            val comprobarNumero = bdHelper.ConsultaValidarExistenciaDelaCuentaPorTelefono(numero)
            val contrasena = contrasenaEditText.text.toString()
            if(comprobarNumero == 0){
               val band1 = bdHelper.insertarUsuario(nombre , fechaActual  , ocupacion , idCiudad , numero)
                if(band1){
                    val band2 = bdHelper.insertarCuenta(numero , "0" , contrasena )
                    if(band2){
                        val band3 = bdHelper.insertarColchon(numero , "0")
                        if(band3){
                            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                            val popupHelper = PopupHelperConfirm(this)
                            popupHelper.showConfirmationPopup(btnListo)

                            val intent = Intent(this, MainActivity::class.java)
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                startActivity(intent)
                            }, 100)

                        }else{
                        Toast.makeText(this, "Error al crear Colchon", Toast.LENGTH_SHORT).show()
                    }
                    }else{
                    Toast.makeText(this, "Error al crear Cuenta", Toast.LENGTH_SHORT).show()
                }
                }else{
                    Toast.makeText(this, "Error al crear Usuario", Toast.LENGTH_SHORT).show()
                }

            }

        }


    btnAtras.setOnClickListener{
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    }




    // Validar todos los campos
    private fun validateFields(): Boolean {
        val nombre = nombreEditText.text.toString()
        val ocupacion = ocupacionEditText.text.toString()
        val numero = numeroEditText.text.toString()
        val contra = contrasenaEditText.text.toString()

        val isNombreValid = nombre.length >= 5
        val isOcupacionValid = ocupacion.length >= 8
        val isNumeroValid = numero.length == 10
        val isContrasenaValid = contra.length == 4

        Log.d("Validation", "Nombre: $isNombreValid, Ocupacion: $isOcupacionValid, Numero: $isNumeroValid, Contrasena: $isContrasenaValid, Spinner: $isSpinnerValid")

        return isNombreValid && isOcupacionValid && isNumeroValid && isSpinnerValid && isContrasenaValid
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            btnListo.isEnabled = validateFields()
        }
    }


    private fun valorCiudad(ciudadSeleccionada : String): String {
        var idCiudad: Int = 0

        when (ciudadSeleccionada) {
            "Medellin" -> idCiudad = 1
            "Rionegro" -> idCiudad = 2
            "Envigado" -> idCiudad = 3
            "Bello" -> idCiudad = 4
            "Bogota" -> idCiudad = 5
            "Soacha" -> idCiudad = 6
            "Chía" -> idCiudad = 7
            "Zipaquira" -> idCiudad = 8
            "Ciudad de Panama" -> idCiudad = 9
            "Colón" -> idCiudad = 10
            "San Miguelito" -> idCiudad = 11
            "La Chorrera" -> idCiudad = 12
            "Penonome" -> idCiudad = 13
            "Aguadulce" -> idCiudad = 14
            "Nata" -> idCiudad = 15
            "La Pintada" -> idCiudad = 16
            else -> {
                // Manejo en caso de que no se haya seleccionado una ciudad válida
                idCiudad = -1  // -1 puede representar un valor inválido
            }
        }

        return idCiudad.toString()

    }
}
