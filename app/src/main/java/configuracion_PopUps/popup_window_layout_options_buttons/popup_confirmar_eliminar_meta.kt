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
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R

class PopupHelperConfirmarEliminarMeta(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta: String , nombre_meta: String , cantidad_en_meta: String) {
        // Infla el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_confirmar_eliminar_meta, null)

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
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.argb(250, 216, 0, 129  ))  // Color semitransparente
                visibility = View.VISIBLE
            }

            // Añadir el fondo al decorView
            decorView.addView(backgroundOverlay)


            val frameCancelarEliminarMeta: FrameLayout = popupView.findViewById(R.id.frameCancelarEliminarMeta)
            val frameAceptarEliminarMeta: FrameLayout = popupView.findViewById(R.id.frameAceptarEliminarMeta)

            frameCancelarEliminarMeta.setOnClickListener {
                popupWindow.dismiss()
            }

            frameAceptarEliminarMeta.setOnClickListener {

                val bdHelper = DatabaseHelper(context)
                bdHelper.actualizarEstadoMeta(nombre_meta , "1")
                bdHelper.actualizarSaldoCuenta(cuenta.toLong() , cantidad_en_meta.toLong() )

                popupWindow.dismiss()
                val popupHelper = PopupHelperConfirm(context)
                popupHelper.showConfirmationPopup(frameAceptarEliminarMeta)

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
