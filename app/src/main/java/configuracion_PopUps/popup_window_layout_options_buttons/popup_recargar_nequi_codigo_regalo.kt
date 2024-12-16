package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R

class PopupHelperRechargeNequiGiftCode(private val context: Context) {

    fun showPopup(anchorView: View , cuenta : String) {
        // Inflar el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_recargar_nequi_codigo_regalo, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT, // Ancho del PopupWindow
            ViewGroup.LayoutParams.WRAP_CONTENT // Altura del PopupWindow
        ).apply {
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(null) // Permite interacción fuera del PopupWindow
        }

        // Configurar los botones dentro del PopupWindow
        val btnListoRecargarCodigoRegalo: Button = popupView.findViewById(R.id.btnListoRecargarCodigoRegalo)
        val btnXRechargeNequiGiftCode: ImageButton = popupView.findViewById(R.id.btnX_rechargeNequiGiftCode)

        btnListoRecargarCodigoRegalo.setOnClickListener {
            popupWindow.dismiss()
            val intent = Intent(context, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "codigo_regalo")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
        }

        btnXRechargeNequiGiftCode.setOnClickListener {
            popupWindow.dismiss()
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

            // Posicionamiento del PopupWindow
            val xOffset = 0   // Ajusta la posición horizontal según tus necesidades
            val yOffset = 1000 // Ajusta la posición vertical según tus necesidades

            // Mostrar el PopupWindow en una posición específica usando coordenadas
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, xOffset, yOffset)
        }
    }


}
