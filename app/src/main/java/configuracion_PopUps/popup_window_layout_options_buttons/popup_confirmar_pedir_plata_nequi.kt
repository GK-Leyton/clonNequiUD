package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import com.example.nequi_proyecto_componentes.R

class PopupHelperConfirm(private val context: Context) {

    fun showConfirmationPopup(viewAnchor: View) {
        // Inflar el layout del popup
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_confirmar_pedir_plata_nequi, null)

        // Crear el PopupWindow
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
            popupWindow.dismiss()  // Cierra el popup al hacer clic en la imagen
        }

        // Mostrar el PopupWindow en una posición relativa al anchor (viewAnchor)
        val xOffset = 0  // Desplazamiento horizontal
        val yOffset = -130  // Desplazamiento vertical

        popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, xOffset, yOffset)
    }
}
