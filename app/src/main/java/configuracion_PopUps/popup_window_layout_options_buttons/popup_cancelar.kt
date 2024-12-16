package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.nequi_proyecto_componentes.R

class PopupHelperCancel(private val context: Context) {

    fun showConfirmationPopup(viewAnchor: View) {
        // Infla el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_cancelar, null)

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
        val imageView: ImageView = popupView.findViewById(R.id.imageView2)
        imageView.setOnClickListener {
            // Aquí puedes añadir la lógica que necesites, por ejemplo:
            popupWindow.dismiss()  // Cierra el popup al hacer clic en la imagen
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
                setBackgroundColor(Color.argb(250, 216, 0, 129  ))  // Color semitransparente
                visibility = View.VISIBLE
            }

            // Añadir el fondo al decorView
            decorView.addView(backgroundOverlay)

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
