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
import androidx.appcompat.app.AppCompatActivity
import com.example.nequi_proyecto_componentes.PopupHelperRecargaPSEReal
import com.example.nequi_proyecto_componentes.R

class PopupHelperRecargarNekiPorPSE(private val context: Context) {

    fun showPopup(viewAnchor: View) {
        // Inflar el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_recargar_nequi_pse, null)

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
        val btnListo: Button = popupView.findViewById(R.id.btnListoRecargarPSE)
        btnListo.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRecargaPSEReal(context)
            popupHelper.showPopup(btnListo)
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
            val yOffset = 1000  // Desplazamiento vertical

            popupWindow.showAsDropDown(viewAnchor, xOffset, yOffset, Gravity.CENTER)
        }
    }
}
