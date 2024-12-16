package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.fragment.app.FragmentManager
import com.example.nequi_proyecto_componentes.R

class PopupHelperSeguridad_parte1(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    fun showPopup(viewAnchor: View , cuenta: String) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_seguridad_parte1, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT, // Ancho del PopupWindow
            ViewGroup.LayoutParams.MATCH_PARENT // Altura del PopupWindow
        )

        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null) // Permite interacción fuera del PopupWindow

        // Configura los botones dentro del PopupWindow
        val btnVolverSeguridad: ImageButton = popupView.findViewById(R.id.btnVolverSeguridad)
        val layoutCambiaTuClave: FrameLayout = popupView.findViewById(R.id.layoutCambiaTuClave)

        btnVolverSeguridad.setOnClickListener {
            popupWindow.dismiss()
        }

        layoutCambiaTuClave.setOnClickListener {
            val dialog = DialogCambioContraseña.newInstance(cuenta)
            dialog.show(fragmentManager, "Dialog Contraseña")
        }

        // Obtener la ubicación de la vista ancla
        val location = IntArray(2)
        viewAnchor.getLocationOnScreen(location)

        // Define el desplazamiento vertical para mostrar el PopupWindow más abajo
        val yOffset = 900 // Ajusta este valor según sea necesario

        // Mostrar el PopupWindow con desplazamiento
        popupWindow.showAtLocation(viewAnchor, Gravity.NO_GRAVITY, location[0], location[1] + yOffset)
    }

}
