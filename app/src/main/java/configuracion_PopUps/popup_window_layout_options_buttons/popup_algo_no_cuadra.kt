package com.example.nequi_proyecto_componentes

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.view.ViewGroup  // Asegúrate de importar esta clase
import android.view.ViewGroup.LayoutParams
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperCancel
import androidx.appcompat.app.AppCompatActivity  // Asegúrate de importar esta clase también

class PopupHelperAlgoNoCuadra(private val context: Context) {

    fun showPopup(viewAnchor: View) {
        // Infla el layout personalizado para el PopupWindow
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_window_layout_algo_salio_mal_codigo_regalo, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        // Configura el evento para 'frameAlgoNoCuadra'
        val frameAlgoNoCuadra: FrameLayout = popupView.findViewById(R.id.frameAlgoNoCuadra)
        frameAlgoNoCuadra.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperCancel(context)
            popupHelper.showConfirmationPopup(frameAlgoNoCuadra)
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
