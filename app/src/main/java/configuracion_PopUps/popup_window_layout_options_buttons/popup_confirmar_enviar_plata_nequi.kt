package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nequi_proyecto_componentes.R

class PopupHelperConfirmSendMoneyNequi(private val context: Context) {

    fun showPopup(viewAnchor: View, numeroTelefono: String, cantidadEnviar: String , numeroCuentaEnviar : String , enviar : String , mensaje : String , fechaActual : String , cuenta : String) {
        // Inflar el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_confirmar_enviar_plata_nequi, null)
        val btnSiSeguro: Button = popupView.findViewById(R.id.btnConfirmarEnviarPalataNequi)

        val bdHelper = DatabaseHelper(context)
        val nombreUsuario = bdHelper.obtenerNombreUsuarioPorTelefono(numeroTelefono)


        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Configurar la lógica del botón dentro del popup
        btnSiSeguro.setOnClickListener {
            val popupHelperParte2 = PopupHelperConfirmSendMoneyNequiParte2(context)
            popupHelperParte2.showPopup(btnSiSeguro, numeroTelefono , cantidadEnviar , numeroCuentaEnviar , enviar , mensaje , fechaActual , cuenta , nombreUsuario.toString())
            popupWindow.dismiss()
        }

        // Configurar el mensaje en el popup
        val mensajeConfirmacionEnvioNequi: TextView = popupView.findViewById(R.id.txtMensajeConfirmacionEnvioNequi)
        mensajeConfirmacionEnvioNequi.text = "Vas a enviar plata al $numeroTelefono -\n$nombreUsuario\nSi te equivocas no podemos hacer\nnada desde nequi"

        // Crear y mostrar el fondo semitransparente
        val activity = context as? AppCompatActivity
        val decorView = activity?.window?.decorView as? ViewGroup

        if (decorView != null) {
            val backgroundOverlay = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.argb(150, 0, 0, 0))  // Color semitransparente
                visibility = View.VISIBLE
            }

            // Añadir el fondo al decorView
            decorView.addView(backgroundOverlay)

            // Hacer el PopupWindow enfocable y desechable al tocar fuera de él
            popupWindow.isFocusable = true
            popupWindow.isOutsideTouchable = true
            popupWindow.setBackgroundDrawable(null)

            // Mostrar el PopupWindow en una posición relativa al anchor (viewAnchor)
            val xOffset = 0  // Desplazamiento horizontal
            val yOffset = -220  // Desplazamiento vertical

            popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, xOffset, yOffset)

            // Configurar el listener para cerrar el PopupWindow y el fondo
            popupWindow.setOnDismissListener {
                backgroundOverlay.visibility = View.GONE
                decorView.removeView(backgroundOverlay)
            }

            // Opcional: Cerrar el PopupWindow si se toca el fondo
            backgroundOverlay.setOnClickListener {
                popupWindow.dismiss()
            }
        }
    }
}
