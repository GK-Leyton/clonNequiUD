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
import android.widget.Toast

class PopupHelperAskForMoney(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_pedir_plata_nequi_parte1, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Configura los botones dentro del PopupWindow
        val btnPedirANequi: ImageButton = popupView.findViewById(R.id.btnPedirANequi)
        val btnPedirATranfiya: ImageButton = popupView.findViewById(R.id.btnPedirATranfiya)
        val framePedirANequi: FrameLayout = popupView.findViewById(R.id.framePedirNequi)
        val framePedirATranfiya: FrameLayout = popupView.findViewById(R.id.framePedirTransfiya)
        val btnx: ImageButton = popupView.findViewById(R.id.btnX_askForMoney)




        btnx.setOnClickListener{
            popupWindow.dismiss()
        }

        //Toast.makeText(context,"Valor recivido en popup pedir plata " + cuenta, Toast.LENGTH_SHORT).show()
        btnPedirANequi.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "PedirPlataNequi")
                putExtra("mensaje" , "Pide Plata")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        btnPedirATranfiya.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "PedirPlataTransfiya")
                putExtra("mensaje" , "Pide Plata por Transfiya")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        framePedirANequi.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "PedirPlataNequi")
                putExtra("mensaje" , "Pide Plata")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        framePedirATranfiya.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "PedirPlataTransfiya")
                putExtra("mensaje" , "Pide Plata por Transfiya")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        // Definir desplazamiento
        val xOffset = 0  // Desplazamiento horizontal
        val yOffset = 1800 // Desplazamiento vertical (ajusta según tu diseño)

        // Mostrar el PopupWindow con desplazamiento
        popupWindow.showAtLocation(viewAnchor, Gravity.NO_GRAVITY, xOffset, yOffset)

    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }




}

