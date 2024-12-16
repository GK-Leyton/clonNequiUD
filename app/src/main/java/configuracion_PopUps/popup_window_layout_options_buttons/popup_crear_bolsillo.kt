package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R
import java.time.LocalDate

class PopupHelperCrearBolsillo(private val context: Context) {
    private var popupWindow: PopupWindow? = null

    fun showPopup(anchorView: View , cuenta: String) {
        // Infla el layout del Popup
        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_crear_bolsillo, null)

        val txtCantidad = popupView.findViewById<EditText>(R.id.txtCuantoVasAMeter)
        val txtNombre = popupView.findViewById<EditText>(R.id.txtNombreBolsillo)
        val txtNombreNuevo = popupView.findViewById<TextView>(R.id.txtNombreBolsilloPorCrear)
        val txtCantidadNuevo = popupView.findViewById<TextView>(R.id.txtCantidadEnBolsilloPorCrear)
        val btnSumar = popupView.findViewById<ImageButton>(R.id.btnSumar)
        val btnRestar = popupView.findViewById<ImageButton>(R.id.btnRestar)
        val btnListoColchon = popupView.findViewById<Button>(R.id.btnListoColchon)

        // Sincroniza los valores al mostrar el popup
        txtCantidadNuevo.text = txtCantidad.text.toString()
        txtNombreNuevo.text = txtNombre.text.toString()

        // Inicialmente el botón btnListoColchon está deshabilitado
        btnListoColchon.isEnabled = false

        // Función para validar ambos campos y habilitar/deshabilitar el botón
        fun validateFields() {
            val cantidadValida = txtCantidad.text.toString().toIntOrNull() ?: -1
            val nombreValido = txtNombre.text.toString().length >= 4

            // El botón se habilita solo si txtCuantoVasAMeter >= 0 y txtNombreBolsillo tiene al menos 4 caracteres
            btnListoColchon.isEnabled = (cantidadValida >= 0 && nombreValido)
        }

        // TextWatcher para txtCantidad
        txtCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var aux = ""
                if(s.toString().isEmpty()){
                    aux = ""

                }else{
                    aux = "$"
                }

                txtCantidadNuevo.text = aux+s.toString() // Sincroniza txtCantidadNuevo
                validateFields() // Valida ambos campos
            }

            override fun afterTextChanged(s: Editable?) {
                // No se necesita implementar esto
            }
        })

        // TextWatcher para txtNombre
        txtNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                txtNombreNuevo.text = s.toString() // Sincroniza txtNombreNuevo
                validateFields() // Valida ambos campos
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
                Toast.makeText(context, "Valor inválido", Toast.LENGTH_SHORT).show()
            }
        }
        val handler = Handler(Looper.getMainLooper())//para ejecutar el redireccionamiento despues de unos milisegundos


        btnListoColchon.setOnClickListener {
            popupWindow?.dismiss() // Cierra el popup actual

            val bdHelper = DatabaseHelper(context)
            val totalDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta)



            val cantidad = txtCantidad.text.toString().toIntOrNull() ?: 0
            if (cantidad <= totalDisponible.toString().toInt()) {

                var nombre = txtNombre.text.toString()
                var monto = txtCantidad.text.toString()
                val fechaActual: String = LocalDate.now().toString()
                val cuentaId = cuenta
                val estado = "1"

                val dbHelper = DatabaseHelper(context)
                val band = dbHelper.insertarBolsillo(nombre, monto, fechaActual, cuentaId, estado)
                if(band != (-1).toLong()){
                    val cantidadRestar = monto.toLong() * -1
                    dbHelper.actualizarSaldoCuenta(cuentaId.toLong(), cantidadRestar)
                }
                // Toast.makeText(context, "Bolsillo creado " + band, Toast.LENGTH_SHORT).show()




                //*creacion del bolsillo
                val popupHelper = PopupHelperConfirm(context)
                popupHelper.showConfirmationPopup(anchorView) // Asegúrate de pasar la vista adecuada



                handler.postDelayed({
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    context.startActivity(intent)
                }, 100)

            } else {
                val popupHelper = PopupHelperCancel(context)
                popupHelper.showConfirmationPopup(anchorView) // Asegúrate de pasar la vista adecuada

                handler.postDelayed({
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    context.startActivity(intent)
                }, 100)
            }
        }

        // Configura el PopupWindow
        popupWindow = PopupWindow(
            popupView,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            isFocusable = true
            isOutsideTouchable = false // Para que no se cierre al tocar fuera del popup
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Fondo transparente
        }

        // Configurar botón para cerrar el Popup
        val closeButton = popupView.findViewById<ImageButton>(R.id.btnVoler)
        closeButton.setOnClickListener {
            popupWindow?.dismiss()
        }

        val xOffset = 0
        val yOffset = -500
        // Muestra el PopupWindow
        popupWindow?.showAtLocation(anchorView, Gravity.CENTER, xOffset, yOffset)
    }
}
