package configuracion_PopUps.popup_window_layout_options_buttons

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.nequi_proyecto_componentes.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PopupHelperCrearMeta(private val context: Context ) {

    fun showPopup(viewAnchor: View , cuenta: String) {
        // Infla el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_crear_meta, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true // focusable
        )

        // Referencias a los componentes del popup
        val btnVolver = popupView.findViewById<ImageButton>(R.id.btnVoler)
        val txtNombreMeta = popupView.findViewById<TextView>(R.id.txtNombreMetaPorCrear)
        val txtCantidadMeta = popupView.findViewById<EditText>(R.id.txtCantidadMetaPorCrear)
        val txtNombreMensaje = popupView.findViewById<TextView>(R.id.txtNombreMensaje)
        val txtNombreBolsillo = popupView.findViewById<EditText>(R.id.txtNombreBolsillo)
        val btnListoCrearMeta = popupView.findViewById<Button>(R.id.btnListoCrearMeta)
        val FechaObjetivo = popupView.findViewById<FrameLayout>(R.id.frameFecha)
        val txtFecha = popupView.findViewById<TextView>(R.id.FechaObjetivoTexto)


        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = Calendar.getInstance().time
        val fechaActualStr = sdf.format(fechaActual)




        FechaObjetivo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Crear el DatePickerDialog
            val datePickerDialog = DatePickerDialog(context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Formatear la fecha seleccionada
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val formattedDate = sdf.format(selectedDate.time)

                    // Comparar fechas
                    val selectedDateObj = sdf.parse(formattedDate)
                    val fechaActualObj = sdf.parse(fechaActualStr)

                    if (selectedDateObj != null && fechaActualObj != null) {
                        if (selectedDateObj.after(fechaActualObj)) {
                            txtFecha.text = formattedDate
                        } else {
                            // Manejar caso en el que la fecha seleccionada es antes de la fecha actual
                            Toast.makeText(context, "La fecha seleccionada debe ser posterior a la fecha actual.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                year, month, day
            )

            // Mostrar el DatePickerDialog
            datePickerDialog.show()
        }

        // Deshabilitar el botón inicialmente
        btnListoCrearMeta.isEnabled = false

        // TextWatcher para habilitar el botón cuando se cumplan las condiciones
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Actualiza el valor de txtNombreMeta con el valor de txtNombreBolsillo
                txtNombreMeta.text = txtNombreBolsillo.text.toString()

                // Verifica las condiciones para habilitar el botón
                val nombreBolsillo = txtNombreBolsillo.text.toString().trim()
                val cantidadMeta = txtCantidadMeta.text.toString().toIntOrNull()
                val fecha = txtFecha.text.toString().trim()

                btnListoCrearMeta.isEnabled = nombreBolsillo.isNotEmpty() && (cantidadMeta != null && cantidadMeta > 50) && (fecha.isNotEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        // Añadir el TextWatcher a los campos de texto
        txtNombreBolsillo.addTextChangedListener(textWatcher)
        txtCantidadMeta.addTextChangedListener(textWatcher)
        txtFecha.addTextChangedListener(textWatcher)
        // Configura la acción del botón de volver
        btnVolver.setOnClickListener {
            popupWindow.dismiss()
        }

        // Configura el botón "Listo" (puedes añadir tu lógica aquí)
        btnListoCrearMeta.setOnClickListener {

            val nombre = txtNombreBolsillo.text.toString()
            val montoActual = "0"
            val montoObjetivo = txtCantidadMeta.text.toString()
            val fechaCreacion = fechaActualStr.toString()
            val cuentaId = cuenta
            val fechaObjetivo = txtFecha.text.toString()
            val bdHelper = DatabaseHelper(context)

            val band = bdHelper.insertarMeta(nombre, montoActual.toDouble(), montoObjetivo.toDouble(), fechaCreacion, fechaObjetivo , cuentaId, "6")

            //Toast.makeText(context, "ID Meta" + band, Toast.LENGTH_SHORT).show()

            popupWindow.dismiss() // Cierra el popup
            val popupHelper = PopupHelperConfirm(context)
            popupHelper.showConfirmationPopup(btnListoCrearMeta)
        }

        // Mostrar el PopupWindow en una posición relativa al anchor (viewAnchor)
        popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, 0, 0)
    }
}
