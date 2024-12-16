package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R

class PopupHelperConfirmSendMoneyTransfiyaParte2(private val context: Context) {

    fun showPopup(anchorView: View, numeroTelefono: String, cantidadEnviar: String , numeroCuentaEnviar : String , enviar : String , mensaje : String , fechaActual : String , cuenta : String) {
        // Inflar el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_confirmar_enviar_plata_transfiya_parte2, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(null)
        }

        // Configurar los elementos del popup
        val btnConfirmar: Button = popupView.findViewById(R.id.btnConfirmarEnvioTransfiyaparte2)
        val btnClose: ImageButton = popupView.findViewById(R.id.btnX_confirmarEnvioNequiParte2)
        val telefono: TextView = popupView.findViewById(R.id.txtCelularEnvioTranfiParte2)
        val cantidad: TextView = popupView.findViewById(R.id.txtCantidadEnvioTrasfiParte2)

        cantidad.text = cantidadEnviar
        telefono.text = numeroTelefono
        val handler = Handler(Looper.getMainLooper())

        // Configurar los eventos de clic
        btnConfirmar.setOnClickListener {

            val bdHelper = DatabaseHelper(context)
            bdHelper.actualizarSaldoCuenta(cuenta.toLong(), enviar.toLong() * -1)
            bdHelper.actualizarSaldoCuenta(numeroCuentaEnviar.toLong(), enviar.toLong())
            bdHelper.insertarEnvio(numeroCuentaEnviar , enviar.toString() , mensaje , fechaActual , cuenta)
            popupWindow.dismiss()

            val popupHelper = PopupHelperConfirm(context)
            popupHelper.showConfirmationPopup(btnConfirmar)

            handler.postDelayed({
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", cuenta)
                }
                context.startActivity(intent)
            }, 100)
        }

        btnClose.setOnClickListener {
            popupWindow.dismiss()
            handler.postDelayed({
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", cuenta)
                }
                context.startActivity(intent)
            }, 100)
        }

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

            // Configurar el PopupWindow para que se cierre al tocar el fondo
            popupWindow.setOnDismissListener {
                backgroundOverlay.visibility = View.GONE
                decorView.removeView(backgroundOverlay)
            }

            backgroundOverlay.setOnClickListener {
                popupWindow.dismiss()
            }

            // Mostrar el PopupWindow en la posición deseada
            val xOffset = 0  // Desplazamiento horizontal
            val yOffset = -950  // Desplazamiento vertical

            popupWindow.showAsDropDown(anchorView, xOffset, yOffset, Gravity.CENTER)
        }
    }
}
