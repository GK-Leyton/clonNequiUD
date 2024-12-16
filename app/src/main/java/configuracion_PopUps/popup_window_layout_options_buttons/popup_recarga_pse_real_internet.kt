package com.example.nequi_proyecto_componentes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.PopupWindow

class PopupHelperRecargaPSEReal(private val context: Context) {

    fun showPopup(viewAnchor: View) {
        // Inflar el layout del PopupWindow desde el archivo XML
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_recarga_pse_real_internet, null)

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
        val btnVolver: ImageButton = popupView.findViewById(R.id.btnVolverRecargaPSEReal)
        val webView: WebView = popupView.findViewById(R.id.WVRecargaPSEReal)

        // Cargar contenido en el WebView
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://clientes.nequi.com.co/recargas")


        btnVolver.setOnClickListener {
            popupWindow.dismiss()  // Cierra el PopupWindow cuando se presiona el botón
        }

        // Mostrar el PopupWindow
        val xOffset = 300
        val yOffset = -980
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, xOffset, yOffset)
    }
}
