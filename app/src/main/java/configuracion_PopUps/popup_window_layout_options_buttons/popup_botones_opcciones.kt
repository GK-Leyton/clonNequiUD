package com.example.nequi_proyecto_componentes

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperRechargeNequi
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperWithdrawMoney

class PopupButtonWindowHelper(private val context: Context) {

    fun showPopup(imageButton: ImageButton , cuenta : String) {
        // Infla el layout personalizado para el PopupWindow
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_window_layout_botones_opcciones, null)
        val popupWindow = PopupWindow(popupView, WRAP_CONTENT, WRAP_CONTENT)

        // Configura los botones en el PopupWindow
        val btnOption1: ImageButton = popupView.findViewById(R.id.btnOption2)
        val btnOption2: ImageButton = popupView.findViewById(R.id.btnOption3)
        val btnOption3: ImageButton = popupView.findViewById(R.id.btnOption4)
        val btnOption4: ImageButton = popupView.findViewById(R.id.btnOption5)
        //Toast.makeText(context,"Valor recivido en boton opcciones " + cuenta, Toast.LENGTH_SHORT).show()
        btnOption1.setOnClickListener {
            val popupHelper = PopupHelperWithdrawMoney(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }

        btnOption2.setOnClickListener {
            val popupHelper = PopupHelperAskForMoney(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }

        btnOption3.setOnClickListener {
            val popupHelper = PopupHelperSendMoney(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }

        btnOption4.setOnClickListener {
            val popupHelper = PopupHelperRechargeNequi(context)
            popupHelper.showPopup(imageButton , cuenta)
            popupWindow.dismiss()
        }

        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))  // Permite interacción fuera del PopupWindow

        // Mostrar el PopupWindow hacia arriba
        val location = IntArray(2)
        imageButton.getLocationOnScreen(location)

        val xOffset = 0
        val yOffset = -(popupView.height) // Ajusta el valor si es necesario

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
            popupWindow.showAtLocation(imageButton, Gravity.NO_GRAVITY, location[0] + xOffset, location[1] - popupView.height - 20) // Ajusta el -20 si es necesario

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

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
