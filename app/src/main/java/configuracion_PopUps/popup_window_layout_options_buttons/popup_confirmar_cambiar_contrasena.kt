package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R

class PopupHelperConfirmarCambiarContrasena(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta: String , contrasena: String) {
        // Infla el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_confirmar_cambiar_contrasena, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Hacer el PopupWindow enfocable y desechable al tocar fuera de él
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Configurar la lógica de los elementos del popup si es necesario


        // Crear y mostrar el fondo semitransparente
        val activity = context as? AppCompatActivity
        val decorView = activity?.window?.decorView as? ViewGroup

        if (decorView != null) {
            val backgroundOverlay = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(Color.argb(250, 216, 0, 129  ))  // Color semitransparente
                visibility = View.VISIBLE
            }

            // Añadir el fondo al decorView
            decorView.addView(backgroundOverlay)


            val frameCancelarEditarContrasena: FrameLayout = popupView.findViewById(R.id.frameCancelarEditarContrasena)
            val frameAceptarCambiarContrasena: FrameLayout = popupView.findViewById(R.id.frameAceptarEditarContrasena)

            frameCancelarEditarContrasena.setOnClickListener {
                popupWindow.dismiss()

                Thread {
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta" , cuenta)
                    }
                    Thread.sleep(100)
                    context.startActivity(intent)  // Aquí es donde pasamos el contexto correcto
                }.start()
            }

            frameAceptarCambiarContrasena.setOnClickListener {

                val bdHelper = DatabaseHelper(context)
                bdHelper.actualizarContrasena(cuenta, contrasena)

                popupWindow.dismiss()

                val popupHelper = PopupHelperConfirm(context)
                popupHelper.showConfirmationPopup(viewAnchor)

                Thread {
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta" , cuenta)
                    }
                    Thread.sleep(500)
                    context.startActivity(intent)  // Aquí es donde pasamos el contexto correcto
                }.start()
            }





            // Mostrar el PopupWindow en una posición relativa al anchor (viewAnchor)
            val xOffset = 0  // Desplazamiento horizontal
            val yOffset = -130  // Desplazamiento vertical
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
