package com.example.nequi_proyecto_componentes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop

class PopupHelperInformacionPeticion(private val context: Context) {

    fun showPopup(viewAnchor: View, mensaje: String, mensaje2: String , monto : String) {
        // Inflar el layout del PopupWindow desde el archivo XML
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_informacion_notificacion_peticion, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT // Se ajusta al contenido
        )

        // Configurar el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null) // Ajusta si necesitas fondo

        // Configurar los elementos dentro del PopupWindow
        val btnVolver: ImageButton = popupView.findViewById(R.id.btnVolverInformacionNotificacionPeticionRechazada)
        val txtMensaje: TextView = popupView.findViewById(R.id.txtMensajePeticion)
        val txtMensaje2: TextView = popupView.findViewById(R.id.txtMensajePeticion2)
        val layoutContenedorManito = popupView.findViewById<FrameLayout>(R.id.framelayoutDescriptorVisualEstadoPeticion)
        var imgManito = popupView.findViewById<ImageView>(R.id.imgManito)
        var complementoTxtMensaje2 = "rechazado"
        if(mensaje.equals("Su peticion ha sido aceptada.")){
            layoutContenedorManito.background = context.getDrawable(R.drawable.fondo_para_peticion_aceptada_notificaciones)
            imgManito.setImageResource(R.drawable.icono_pulgar_arriba)
            //ajustar el margin top de imgManito a un valor nuevo (sugerido 15dp)

            val params = imgManito.layoutParams as FrameLayout.LayoutParams
            val marginInDp = 8 * context.resources.displayMetrics.density // Convertir 15dp a px
            params.topMargin = marginInDp.toInt()
            imgManito.layoutParams = params
            complementoTxtMensaje2 = "aceptado"
        }

        // Establecer los mensajes pasados como parámetros
        txtMensaje.text = mensaje
        txtMensaje2.text = mensaje2 + " ha "+complementoTxtMensaje2+" tu peticion \npor $"+monto+"."

        // Botón para cerrar el PopupWindow
        btnVolver.setOnClickListener {
            popupWindow.dismiss()
        }

        // Mostrar el PopupWindow
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, 0, 0)
    }
}
