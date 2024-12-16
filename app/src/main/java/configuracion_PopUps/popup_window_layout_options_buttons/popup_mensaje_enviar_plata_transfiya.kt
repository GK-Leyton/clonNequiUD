package com.example.nequi_proyecto_componentes

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

class PopupHelperSendMoneyTransfiyaDialog(private val context: Context) {

    fun showPopup(anchorView: View , cuenta : String) {
        // Infla el layout personalizado para el PopupWindow
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_window_layout_mensaje_confirmar_enviar_plata_transfiya, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        val atras: ImageButton = popupView.findViewById(R.id.btnAtrasTrasfiyaDialog)
        val btnConfirm: Button = popupView.findViewById(R.id.btnConfirmacionEnvTransf)

        atras.setOnClickListener {
            popupWindow.dismiss()
        }

        btnConfirm.setOnClickListener {
            val intent = Intent(context, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "enviar_plata_transfiya")
                putExtra("mensaje", "Envía plata Transfiya")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss() // Cierra el popup actual
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
        }

        // Mide el popupView antes de usar su altura
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = popupView.measuredHeight
        val popupWidth = popupView.measuredWidth

        // Obtiene la ubicación del ancla en la pantalla
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        // Calcula las coordenadas para mostrar el popup
        val xOffset = 0 // Si quieres mover el popup horizontalmente, ajusta este valor
        val yOffset = -100 // Ajusta el -100 para mover el popup hacia arriba o hacia abajo

        // Muestra el PopupWindow en la ubicación calculada
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0] + xOffset, location[1] + yOffset)
    }
}
