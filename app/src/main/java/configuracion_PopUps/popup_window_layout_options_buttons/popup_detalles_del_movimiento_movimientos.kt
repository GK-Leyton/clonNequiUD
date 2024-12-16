import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import com.example.nequi_proyecto_componentes.R
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper

class PopupDetallesDelMovimientoMovimientos(private val context: Context) {

    fun showPopup(anchorView: View, referencia: String , cuenta: String , notificacion: Boolean = false) {
        // Infla el layout del PopupWindow
        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_detalles_movimiento_movimientos, null)

        // Configura los datos en el layout del PopupWindow
        val txtCompaniaDetalle = popupView.findViewById<TextView>(R.id.txtCompañiaDetalleMovimiento)

        val txtValorDetalle = popupView.findViewById<TextView>(R.id.txtFechaMovimiento)
        val txtReferenciaDetalle = popupView.findViewById<TextView>(R.id.txtReferenciaMovimiento)
        val txtCantidadDisponible = popupView.findViewById<TextView>(R.id.textView56)

        val bdHelper = DatabaseHelper(context)

        if(!notificacion){
            val res = bdHelper.obtenerMovimientoPorId(referencia)
            val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta)



            txtReferenciaDetalle.text = res?.get("ID").toString()
            txtCompaniaDetalle.text = res?.get("Mensaje").toString()
            txtValorDetalle.text = res?.get("Monto").toString()
            txtCantidadDisponible.text = "$"+cantidadDisponible.toString()
        }else{
            val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta)
            val res = bdHelper.obtenerMensajePorId(referencia)
            txtReferenciaDetalle.text = res?.get("ID").toString()
            txtCompaniaDetalle.text = res?.get("Mensaje").toString()
            txtValorDetalle.text = res?.get("Monto").toString()
            txtCantidadDisponible.text = "$"+cantidadDisponible.toString()


        }


        // Configura el botón de cerrar
        val closeButton = popupView.findViewById<ImageButton>(R.id.imageButton9)

        // Crea el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // Establece el listener para el botón de cerrar
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }

        // Muestra el PopupWindow
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }
}
