package com.example.nequi_proyecto_componentes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow


class PopupHelperCuandoLLegaLaPlata(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Inflar el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_cuando_llega_la_plata, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Configurar el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Configurar los elementos dentro del PopupWindow
        val btnVolver: ImageButton = popupView.findViewById(R.id.imageButton14)
        val btnListo: Button = popupView.findViewById(R.id.button2)

        // Configurar el botón de volver
        btnVolver.setOnClickListener {
            popupWindow.dismiss()
        }

        // Configurar el botón "Listo"
        btnListo.setOnClickListener {
            popupWindow.dismiss()
            val popupHelper = PopupHelperEnvioBancos(context)
            popupHelper.showPopup(btnListo , cuenta)

        }

        // Mostrar el PopupWindow
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, 0, 0)
    }
}
