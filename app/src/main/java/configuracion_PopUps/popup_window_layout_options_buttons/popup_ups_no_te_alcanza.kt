package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.nequi_proyecto_componentes.R

class PopupHelperUpsNoTeAlcanza(private val context: Context) {

    fun showPopup(viewAnchor: View) {
        // Infla el layout del popup
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_window_layout_mensaje_ups_no_te_alcanza, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Fondo transparente del PopupWindow
        }

        // Configura el botón en el PopupWindow
        val btnListoNoFondosSuficientes: Button = popupView.findViewById(R.id.btnListoNoFondosSuficientes)

        btnListoNoFondosSuficientes.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperCancel(context)
            popupHelper.showConfirmationPopup(btnListoNoFondosSuficientes) // Mostrar otro popup para confirmación
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

            // Mostrar el PopupWindow
            popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, 0, 0)

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
