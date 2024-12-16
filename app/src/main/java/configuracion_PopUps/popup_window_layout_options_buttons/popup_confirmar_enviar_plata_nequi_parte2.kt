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

class PopupHelperConfirmSendMoneyNequiParte2(private val context: Context) {

    fun showPopup(viewAnchor: View , numeroTelefono: String, cantidadEnviar: String , numeroCuentaEnviar : String , enviar : String , mensaje : String , fechaActual : String , cuenta : String , nombre : String) {
        // Inflar el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_confirmar_enviar_plata_nequi_parte2, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Obtener referencias a los elementos de la vista del popup
        val btnConfirmarEnvio: Button = popupView.findViewById(R.id.btnConfirmarEnvioNequiparte2)
        val btnCancelarEnvio: Button = popupView.findViewById(R.id.btnCancelarEnvioNequiparte2)
        val btnCerrarPopup: ImageButton = popupView.findViewById(R.id.btnX_confirmarEnvioNequiParte2)
        val textoNombrePersona: TextView = popupView.findViewById(R.id.txtNombreEnvioParte2)
        val textoNumeroTelefono: TextView = popupView.findViewById(R.id.txtNumeroEnvioNequiParte2)
        val textoCantidadEnviar: TextView = popupView.findViewById(R.id.txtCantidadEnvioNequiParte2)

        // Configurar los valores de los TextViews
        textoNombrePersona.text = nombre
        textoNumeroTelefono.text = numeroTelefono
        textoCantidadEnviar.text = cantidadEnviar
        val handler = Handler(Looper.getMainLooper())

        // Configurar el evento de clic en el botón de confirmar
        btnConfirmarEnvio.setOnClickListener {
            val bdHelper = DatabaseHelper(context)
            popupWindow.dismiss()

            bdHelper.actualizarSaldoCuenta(cuenta.toLong(), enviar.toLong() * -1)
            bdHelper.actualizarSaldoCuenta(numeroCuentaEnviar.toLong(), enviar.toLong())
            bdHelper.insertarEnvio(numeroCuentaEnviar , enviar.toString() , mensaje , fechaActual , cuenta)
            //llamar el popup de confirmacion
            val popupHelper = PopupHelperConfirm(context)
            popupHelper.showConfirmationPopup(btnConfirmarEnvio)

            handler.postDelayed({
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", cuenta)
                }
                context.startActivity(intent)
            }, 100)

        }

        btnCancelarEnvio.setOnClickListener {
            popupWindow.dismiss()
            handler.postDelayed({
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", cuenta)
                }
                context.startActivity(intent)
            }, 100)
        }

        btnCerrarPopup.setOnClickListener {
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

            // Hacer el PopupWindow enfocable y desechable al tocar fuera de él
            popupWindow.isFocusable = true
            popupWindow.isOutsideTouchable = true
            popupWindow.setBackgroundDrawable(null)

            // Mostrar el PopupWindow justo debajo del botón (viewAnchor)
            val xOffset = 0  // Desplazamiento horizontal
            val yOffset = -500  // Desplazamiento vertical

            popupWindow.showAsDropDown(viewAnchor, xOffset, yOffset, Gravity.CENTER)

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
