package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R

class PopupHelperRecargarNequiEfectivo(private val context: Context) {

    fun showPopup(anchorView: View , cuenta : String) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_recargar_nequi_efectivo, null)

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

        // Configura el botón dentro del PopupWindow
        val btnRecargarNequiEfectivo: Button = popupView.findViewById(R.id.btnRecargarNequiEfectivo)
        val btnAtras: ImageButton = popupView.findViewById(R.id.btnAtrasTrasfiyaDialog)

        btnAtras.setOnClickListener{
            popupWindow.dismiss()
        }

        btnRecargarNequiEfectivo.setOnClickListener {
            popupWindow.dismiss()

            val intent = Intent(context, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "mapa")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)

        }

        // Posicionamiento del PopupWindow
        val xOffset = 0   // Ajusta la posición horizontal según tus necesidades
        val yOffset = 1000 // Ajusta la posición vertical según tus necesidades

        // Mostrar el PopupWindow en una posición específica usando coordenadas
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, xOffset, yOffset)
    }
}
