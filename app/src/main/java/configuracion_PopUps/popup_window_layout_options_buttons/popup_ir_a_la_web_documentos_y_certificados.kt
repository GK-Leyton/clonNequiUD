package com.example.nequi_proyecto_componentes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.PopupWindow


class PopupHelperIrALaWebDocumentosYCertificados(private val context: Context) {

    fun showPopup(viewAnchor: View) {
        // Inflar el layout del PopupWindow desde el archivo XML
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_ir_a_la_web_documentos_y_certificados, null)

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
        val btnVolver: ImageButton = popupView.findViewById(R.id.btnVolverPaginaWebDocumentosYCertificados)
        val webView: WebView = popupView.findViewById(R.id.WVPaginaWebDocumentosYCertificados)

        // Cargar contenido en el WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true // Permite almacenamiento en DOM
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        webView.loadUrl("https://transacciones.nequi.com/bdigital/login.jsp")


        btnVolver.setOnClickListener {
            popupWindow.dismiss()  // Cierra el PopupWindow cuando se presiona el botón
        }

        // Mostrar el PopupWindow
        val xOffset = 300
        val yOffset = -980
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, xOffset, yOffset)
    }
}
