package com.example.nequi_proyecto_componentes

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.PopupWindow
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout


class PopupHelperSendMoney(private val context: Context) {

    fun showPopup(imageButton: ImageButton , cuenta : String) {
        // Infla el layout personalizado para el PopupWindow
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_window_layout_enviar_plata_parte1, null)
        val popupWindow = PopupWindow(popupView, WRAP_CONTENT, WRAP_CONTENT)

        // Configura los botones en el PopupWindow
        val EnviarNequi: ImageButton = popupView.findViewById(R.id.btnEnviarANequi)
        val EnviarTransfiya: ImageButton = popupView.findViewById(R.id.btnEnviarATranfiya)
        val EnviarBanco: ImageButton = popupView.findViewById(R.id.btnEnviarABancos)
        val frameEnviarNequi: FrameLayout = popupView.findViewById(R.id.frameEnviarNequi)
        val frameEnviarTransfiya: FrameLayout = popupView.findViewById(R.id.frameEnviarTransfiya)
        val frameEnviarBanco: FrameLayout = popupView.findViewById(R.id.frameEnviarbanco)
        val btnx: ImageButton = popupView.findViewById(R.id.btnX_sendMoney)

        EnviarNequi.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "enviar_plata")
                putExtra("mensaje" , "Envía plata")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }
        EnviarTransfiya.setOnClickListener {
            val popupHelper = PopupHelperSendMoneyTransfiyaDialog(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }
        EnviarBanco.setOnClickListener {
            val popupHelper = PopupHelperCuandoLLegaLaPlata(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }


        frameEnviarNequi.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "enviar_plata")
                putExtra("mensaje" , "Envía plata")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }
        frameEnviarTransfiya.setOnClickListener {
            val popupHelper = PopupHelperSendMoneyTransfiyaDialog(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }
        frameEnviarBanco.setOnClickListener {
            val popupHelper = PopupHelperCuandoLLegaLaPlata(context)
            popupHelper.showPopup(imageButton , cuenta)  // Usa imageButton como ancla para el segundo PopupWindow
            popupWindow.dismiss()
        }

        btnx.setOnClickListener{
            popupWindow.dismiss()
        }





        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)  // Permite interacción fuera del PopupWindow

        // Mostrar el PopupWindow hacia arriba
        val location = IntArray(2)
        imageButton.getLocationOnScreen(location)

        val xOffset = 0
        val yOffset = -(popupView.height) // Ajusta el valor si es necesario

        popupWindow.showAtLocation(imageButton, Gravity.NO_GRAVITY, location[0] + xOffset, location[1] - popupView.height - 20) // Ajusta el -20 si es necesario
    }


}