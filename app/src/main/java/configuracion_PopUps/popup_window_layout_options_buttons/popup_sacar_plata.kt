package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R

class PopupHelperWithdrawMoney(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Infla el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_codigo_sacar_plata_parte1, null)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT, // Ancho del PopupWindow
            ViewGroup.LayoutParams.WRAP_CONTENT // Altura del PopupWindow
        )

        // Configura el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null) // Permite interacción fuera del PopupWindow

        // Configura los botones dentro del PopupWindow
        val btnOption1: ImageButton = popupView.findViewById(R.id.imageButton6)
        val btnOption2: ImageButton = popupView.findViewById(R.id.imageButton7)
        val btnOption3: ImageButton = popupView.findViewById(R.id.imageButton8)
        val btnx: ImageButton = popupView.findViewById(R.id.btnX_withdrawMoney)


        val frameCajero: FrameLayout = popupView.findViewById(R.id.frameLayoutCajero)
        val framePuntoFisico: FrameLayout = popupView.findViewById(R.id.frameLayoutPuntoFisico)
        btnx.setOnClickListener{
            popupWindow.dismiss()
        }

        btnOption1.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "codigo_confirmacion_retiro_cajero")
                putExtra("mensaje" , "Escribe este número en el cajero para sacar plata")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        btnOption2.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "codigo_confirmacion_retiro_punto_fisico")
                putExtra("mensaje" , "Para sacar, confirmale este codigo a la persona encargada del corresponsal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        frameCajero.setOnClickListener {

            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "codigo_confirmacion_retiro_cajero")
                putExtra("mensaje" , "Escribe este número en el cajero para sacar plata")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        framePuntoFisico.setOnClickListener {
            val intent = Intent(context , PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "codigo_confirmacion_retiro_punto_fisico")
                putExtra("mensaje" , "Para sacar, confirmale este codigo a la persona encargada del corresponsal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            context.startActivity(intent)
            popupWindow.dismiss()
        }



        // Obtener la ubicación de la vista ancla
        val location = IntArray(2)
        viewAnchor.getLocationOnScreen(location)

        // Define el desplazamiento vertical para mostrar el PopupWindow más abajo
        val yOffset = -930 // Ajusta este valor según sea necesario

        // Mostrar el PopupWindow con desplazamiento
        popupWindow.showAtLocation(viewAnchor, Gravity.NO_GRAVITY, location[0], location[1] + yOffset)
    }

}
