import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nequi_proyecto_componentes.R
import android.text.Html
import com.example.nequi_proyecto_componentes.PopupHelperIrALaWebDocumentosYCertificados

class PopupHelperDocumentosYCertificadosParte2(private val context: Context) {

    fun showPopup(anchorView: View) {
        // Inflar el layout del popup
        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_documentos_y_certificados_parte2, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            true
        )

        // Configurar los elementos del popup
        val buttonClose = popupView.findViewById<ImageButton>(R.id.imageButton16)
        val btnIrALaWebDocumentosyCertificados2 = popupView.findViewById<Button>(R.id.btnIrALaWebDocumentosyCertificados2)
        val txtHtml = popupView.findViewById<TextView>(R.id.txtConHtml)

        // Obtener el texto HTML desde los recursos
        val htmlText = context.getString(R.string.colored_text)

        // Asignar el texto HTML al TextView
        txtHtml.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)

        buttonClose.setOnClickListener {
            popupWindow.dismiss()
        }

        btnIrALaWebDocumentosyCertificados2.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperIrALaWebDocumentosYCertificados(context)
            popupHelper.showPopup(btnIrALaWebDocumentosyCertificados2)
        }

        // Mostrar el PopupWindow
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0)
    }
}
