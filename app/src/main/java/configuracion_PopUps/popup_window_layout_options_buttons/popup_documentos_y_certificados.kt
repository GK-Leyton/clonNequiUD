package configuracion_PopUps.popup_window_layout_options_buttons

import PopupHelperDocumentosYCertificadosParte2
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import com.example.nequi_proyecto_componentes.R

class PopupHelperDocumentosYCertificados(
    private val context: Context,
) {

    fun showPopup(viewAnchor: View) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_documentos_y_certificados, null)

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
        val btnVolverDocumentosYCertificados: ImageButton = popupView.findViewById(R.id.btnVolverDocumentosYCertificados)
        val layoutDocumentosYCertificados: FrameLayout = popupView.findViewById(R.id.layoutDocumentosYCertificados)
        val btnDocumentosyCertificados: ImageButton = popupView.findViewById(R.id.btnDocumentosYCertificados)


        layoutDocumentosYCertificados.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperDocumentosYCertificadosParte2(context)
            popupHelper.showPopup(layoutDocumentosYCertificados)
        }
        btnVolverDocumentosYCertificados.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperDocumentosYCertificadosParte2(context)
            popupHelper.showPopup(btnVolverDocumentosYCertificados)
        }

        // Obtener la ubicación de la vista ancla
        val location = IntArray(2)
        viewAnchor.getLocationOnScreen(location)

        // Define el desplazamiento vertical para mostrar el PopupWindow más abajo
        val yOffset = 900 // Ajusta este valor según sea necesario

        // Mostrar el PopupWindow con desplazamiento
        popupWindow.showAtLocation(viewAnchor, Gravity.NO_GRAVITY, location[0], location[1] + yOffset)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
