package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import com.example.nequi_proyecto_componentes.R

class PopupHelperRechargeNequi(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_recargar_nequi_parte1, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT, // Ancho del PopupWindow
            ViewGroup.LayoutParams.WRAP_CONTENT // Altura del PopupWindow
        )

        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null) // Permite interacción fuera del PopupWindow

        // Configura los botones dentro del PopupWindow
        val btnOption1: ImageButton = popupView.findViewById(R.id.btnRecargarEfectivo)
        val btnOption2: ImageButton = popupView.findViewById(R.id.btnRecargarDesdeOtroBanco)
        val btnOption3: ImageButton = popupView.findViewById(R.id.btnRecargarCodigoRegalo)
        val btnx: ImageButton = popupView.findViewById(R.id.btnX_rechargeNequi)



        val frameEfectivo: FrameLayout = popupView.findViewById(R.id.frameLayoutRecargaEfectivo)
        val frameDesdeOtroBanco: FrameLayout = popupView.findViewById(R.id.frameLayoutRecargaDesdeOtroBanco)
        val frameCodigoRegalo: FrameLayout = popupView.findViewById(R.id.frameLayoutCodigoRegalo)


        btnx.setOnClickListener{
            popupWindow.dismiss()
        }

        btnOption1.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRecargarNequiEfectivo(context)
            popupHelper.showPopup(btnOption1 , cuenta)

        }

        btnOption2.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRecargarNekiPorPSE(context)
            popupHelper.showPopup(btnOption2)
        }

        btnOption3.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRechargeNequiGiftCode(context)
            popupHelper.showPopup(btnOption3 , cuenta)
        }


        frameEfectivo.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRecargarNequiEfectivo(context)
            popupHelper.showPopup(btnOption1 , cuenta)
        }

        frameDesdeOtroBanco.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRecargarNekiPorPSE(context)
            popupHelper.showPopup(frameDesdeOtroBanco)
        }

        frameCodigoRegalo.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperRechargeNequiGiftCode(context)
            popupHelper.showPopup(frameCodigoRegalo , cuenta)
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
