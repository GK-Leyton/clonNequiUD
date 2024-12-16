package com.example.nequi_proyecto_componentes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import kotlin.random.Random

class PopupHelperClaveDinamica(private val context: Context) {

    // Definir variables globales
    private var progressBar: RingProgressBar? = null
    private var txtClaveDinamica: TextView? = null
    private var valorRandom : String? = null

    fun showPopup(viewAnchor: View) {
        // Inflar el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_clave_dinamica, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Configurar el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Configurar los elementos dentro del PopupWindow
        val btnVolver: ImageButton = popupView.findViewById(R.id.btnVolver)
        progressBar = popupView.findViewById(R.id.progressBarClaveDinamica)
        txtClaveDinamica = popupView.findViewById(R.id.txtClaveDinamica)
        val textView76: TextView = popupView.findViewById(R.id.textView76)

        // Configurar el botón de volver
        btnVolver.setOnClickListener {
            popupWindow.dismiss()
        }

        // Configurar el progreso manualmente
        progressBar?.apply {
            setMaxProgress(60) // Define el valor máximo del progreso usando setMaxProgress()
        }

        // Configurar el texto

        // Mostrar el PopupWindow
        val xOffset =300
        val yOffset = -2000
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, xOffset, yOffset)
    }

    // Método adicional para actualizar el progreso
    fun updateProgressBar(prog: Int) {
        progressBar?.apply {
            setProgress(prog) // Usa el método setProgress para definir el progreso
        }
        if(prog == 1){
            //actualizar el valor de la clave dinamica en la base de datos

            valorRandom = Random.nextInt(10000, 100000).toString()
        }
        txtClaveDinamica?.setText(valorRandom)
    }
}
