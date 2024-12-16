package com.example.nequi_proyecto_componentes

import PopupDetallesDelMovimientoMovimientos
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper

class PopupHelperMovimientos(private val context: Context) {

    private var currentPopupWindow: PopupWindow? = null

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Verifica que la vista ancla no sea nula y que la actividad esté activa
        if (viewAnchor.windowToken == null) {
            return // No se puede mostrar el PopupWindow si la vista ancla no es válida
        }

        // Si ya hay un PopupWindow visible, ciérralo
        currentPopupWindow?.dismiss()

        // Infla el layout del PopupWindow
        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_movimientos_parte1, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            1880,
            true
        ).apply {
            // Configura el PopupWindow
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.WHITE)) // Fondo blanco opaco
        }

        // Encontrar el LinearLayout dentro del ScrollView
        val linearLayout = popupView.findViewById<LinearLayout>(R.id.linearLayout)

        // Agregar contenido dinámico
        addContentToScrollView(linearLayout , cuenta)

        val xOffset = 0
        val yOffset = -170
        // Mostrar el PopupWindow centrado
        popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, xOffset, yOffset)

        // Almacena la referencia del PopupWindow actual
        currentPopupWindow = popupWindow
    }

    private fun addContentToScrollView(linearLayout: LinearLayout , cuenta : String) {
        val inflater = LayoutInflater.from(context)
        val bdHelper = DatabaseHelper(context)
        val resultado = bdHelper.obtenerEnviosPorCuenta(cuenta)
        for(res in resultado){
            //****

            // Infla el layout del item desde el archivo XML
            val itemView = inflater.inflate(R.layout.item_layout_movimientos, linearLayout, false)

            // Configura los elementos del FrameLayout
            val icono = itemView.findViewById<ImageView>(R.id.iconoIngresoEgreso)
            val txtCompania = itemView.findViewById<TextView>(R.id.txtCompaniaMovimientoItem)
            val txtDetalles = itemView.findViewById<TextView>(R.id.txtDetallesMovimientoItem)
            val txtValor = itemView.findViewById<TextView>(R.id.txtValorMovimientoItem)

            if (res["Titulo"] == "Recibiendo") {
                icono.setImageResource(R.drawable.icono_carga2)
            }

            // Establece datos dinámicos en cada TextView
            txtCompania.text = res["Titulo"].toString()
            txtDetalles.text = res["Mensaje"].toString()
            txtValor.text = "$" + res["Monto"].toString()

            // Establece el evento OnClick para cada item
            itemView.setOnClickListener {
                // Cierra el PopupWindow actual antes de abrir uno nuevo
                currentPopupWindow?.dismiss()
                val id = res["ID"].toString()
                val popupHelper = PopupDetallesDelMovimientoMovimientos(context)
                popupHelper.showPopup(it, id , cuenta , false)
            }

            // Agrega el FrameLayout al LinearLayout del ScrollView
            linearLayout.addView(itemView)

            //****
        }





    }
}
