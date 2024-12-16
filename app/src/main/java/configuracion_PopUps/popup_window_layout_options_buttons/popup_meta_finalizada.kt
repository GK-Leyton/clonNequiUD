package com.example.nequi_proyecto_componentes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.nequi_proyecto_componentes.R

class PopupHelperMetaFinalizada(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta: String , nombre_meta_Recibido :String) {
        // Inflar el layout del popup
        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_meta_finalizada, null)
        val txtMensajeMetaFinalizada = popupView.findViewById<TextView>(R.id.txtMensajeMetaFinalizada)
        txtMensajeMetaFinalizada.text = "Despues de tanto esfuerzo tu meta [" +nombre_meta_Recibido+ "] ha culminado."
        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Configurar el PopupWindow para que sea tocable fuera de él
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Obtener elementos del popup y agregar funcionalidades
        val btnCerrar = popupView.findViewById<ImageButton>(R.id.btnX_MetaFinalizada)
        val btnListo = popupView.findViewById<Button>(R.id.btnListoMetaFinalizada)

        // Configurar acciones de los botones
        btnCerrar.setOnClickListener {
            popupWindow.dismiss()
            //redireccionar por INTENT
        }

        btnListo.setOnClickListener {
            Toast.makeText(context, "¡Meta finalizada!", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()


            Thread {
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "menu_principal")
                    putExtra("numeroDeCuenta", cuenta)
                }
                Thread.sleep(500)
                context.startActivity(intent)
            }.start()

        }

        // Mostrar el PopupWindow en el centro de la pantalla
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, 0, 0)
    }
}
