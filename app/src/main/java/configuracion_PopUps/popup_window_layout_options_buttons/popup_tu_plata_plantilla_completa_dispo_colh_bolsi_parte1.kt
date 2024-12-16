package com.example.nequi_proyecto_componentes

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperTusBolsillosParte1
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperTusMetasParte1

class PopupHelperTuPlataDispoColchBolsi(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_tu_plata_pantalla_completa_dispo_colch_bolsi, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Configura los elementos dentro del PopupWindow
        val btnVolver: ImageButton = popupView.findViewById(R.id.btnVolver_tuPlata_disp_colch_bolsi)
        val btnDisponible: FrameLayout = popupView.findViewById(R.id.layout_tu_plata_disponible)
        val btnColchon: FrameLayout = popupView.findViewById(R.id.layout_tu_plata_colchon)
        val btnBolsillo: FrameLayout = popupView.findViewById(R.id.layout_tu_plata_bolsillo)
        val btnMeta: FrameLayout = popupView.findViewById(R.id.layout_tu_plata_meta)


        // Configura el botón de volver
        btnVolver.setOnClickListener {
            popupWindow.dismiss()
        }

        // Configura los botones de los distintos elementos del PopupWindow

        btnColchon.setOnClickListener {
            val intent = Intent(context, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "acceder_colchon")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
        }

        btnBolsillo.setOnClickListener {
            // Configura el siguiente popup para mostrarse después del cierre
            popupWindow.setOnDismissListener {
                val popupHelper = PopupHelperTusBolsillosParte1(context)
                popupHelper.showPopup(viewAnchor , cuenta.toString())
            }

            // Cierra el popup actual
            popupWindow.dismiss()
        }

        btnMeta.setOnClickListener {
            // Configura el siguiente popup para mostrarse después del cierre
            popupWindow.setOnDismissListener {
                val popupHelper = PopupHelperTusMetasParte1(context , cuenta)
                popupHelper.showPopup(viewAnchor , cuenta)
            }

            // Cierra el popup actual
            popupWindow.dismiss()
        }



        // Definir desplazamiento
        val xOffset = 0  // Desplazamiento horizontal
        val yOffset = 0  // Desplazamiento vertical (ajusta según tu diseño)

        // Mostrar el PopupWindow con desplazamiento
        popupWindow.showAtLocation(viewAnchor, Gravity.NO_GRAVITY, xOffset, yOffset)
    }


}
