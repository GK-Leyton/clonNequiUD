package com.example.nequi_proyecto_componentes

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm

class RecargarNequiCodigoRegalo : AppCompatActivity() {

    private lateinit var btnPedirPlataCodigoRegalo: Button
    private lateinit var txtCodigoRegalo: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recargar_nequi_codigo_regalo)




        // Encuentra las vistas
        btnPedirPlataCodigoRegalo = findViewById(R.id.btn_pedirPlataCodigoRegalo)
        txtCodigoRegalo = findViewById(R.id.txtCodigoRegalo)
        val btnAtras = findViewById<ImageButton>(R.id.btnVolver_recargarPlataCodigoRegalo)
        val btnSigue = findViewById<Button>(R.id.btn_pedirPlataCodigoRegalo)
        val cuenta = intent.getStringExtra("numeroDeCuenta")

        // Configura el clic del botón de regreso
        btnAtras.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina" , "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)


        }
        val handler = Handler(Looper.getMainLooper())

        btnSigue.setOnClickListener{
            hideKeyboard()

            val codigo = txtCodigoRegalo.text.toString()

            val bdHelper = DatabaseHelper(this)
            val datos : Pair<String? , String?>? = bdHelper.obtenerRegaloPorCodigo(codigo)
            val id_regalo = datos?.first.toString()
            val monto_regalo = datos?.second.toString()

            Toast.makeText(this, "El monto del regalo es: $monto_regalo", Toast.LENGTH_SHORT).show()
            //Toast.makeText(this, "El id del regalo es: $id_regalo", Toast.LENGTH_SHORT).show()


            if(datos != null){
                bdHelper.actualizarSaldoCuenta(cuenta.toString().toLong(), monto_regalo.toLong())
                var estado = "0"
                if(id_regalo.toInt() == 10){
                    estado = "1"
                }
                val band = bdHelper.actualizarEstadoRegaloPorId(id_regalo, estado)

                if(band){
                    val popupHelper = PopupHelperConfirm(this)
                    popupHelper.showConfirmationPopup(btnSigue)

                    handler.postDelayed({
                        val intent = Intent(this, PantallaDeCarga::class.java).apply {
                            putExtra("proximaPagina", "menu_principal")
                            putExtra("numeroDeCuenta", cuenta)
                        }
                        startActivity(intent) // Inicia la nueva activity
                    }, 100)
                }else{
                    Toast.makeText(this, "El codigo no es valido o ha expirado", Toast.LENGTH_SHORT).show()
                }


            }else{
                val popupHelper = PopupHelperAlgoNoCuadra(this)
                popupHelper.showPopup(btnSigue)
                Toast.makeText(this, "El codigo no es valido o ha expirado", Toast.LENGTH_SHORT).show()
                handler.postDelayed({
                    val intent = Intent(this, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    startActivity(intent) // Inicia la nueva activity
                }, 100)
            }


        }


        // Configura el TextWatcher para el EditText
        setupTextWatcher()
    }

    private fun setupTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        txtCodigoRegalo.addTextChangedListener(textWatcher)
    }

    private fun updateButtonState() {
        // Obtiene el texto del EditText
        val codigoRegaloText = txtCodigoRegalo.text.toString()

        // Habilita o deshabilita el botón basado en la longitud del texto
        val isValidCodigoRegalo = codigoRegaloText.length >= 10

        btnPedirPlataCodigoRegalo.isEnabled = isValidCodigoRegalo
        val colorStateList = ColorStateList.valueOf(
            if (isValidCodigoRegalo) Color.parseColor("#d80081") else Color.parseColor("#C74E96")
        )
        btnPedirPlataCodigoRegalo.backgroundTintList = colorStateList
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        // Sobrescribe el evento de retroceso con el botón hacia atrás del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}
