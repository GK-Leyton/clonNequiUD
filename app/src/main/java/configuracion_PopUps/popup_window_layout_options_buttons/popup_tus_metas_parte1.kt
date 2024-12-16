package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nequi_proyecto_componentes.R
import com.example.nequi_proyecto_componentes.PantallaDeCarga

class PopupHelperTusMetasParte1(
    val context: Context,
    val cuenta: String,
    val items: List<MetaItem> = generateSampleData(cuenta , context)
) {

    private var currentPopupWindow: PopupWindow? = null

    fun showPopup(viewAnchor: View , cuenta : String) {
        if (viewAnchor.windowToken == null) return

        currentPopupWindow?.dismiss()

        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_tus_metas_parte1, null)


        val btnCrearMeta = popupView.findViewById<FrameLayout>(R.id.frameCrearMeta)
        val btnCrearMeta2 = popupView.findViewById<ImageButton>(R.id.btnCrearMeta)
        btnCrearMeta.setOnClickListener {
            currentPopupWindow?.dismiss()
            val popupHelper = PopupHelperCrearMeta(context)
            popupHelper.showPopup(btnCrearMeta , cuenta)
        }
        btnCrearMeta2.setOnClickListener {
            currentPopupWindow?.dismiss()
            val popupHelper = PopupHelperCrearMeta(context)
            popupHelper.showPopup(btnCrearMeta , cuenta)
        }




        setupRecyclerView(popupView)


        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            isFocusable = true
            isOutsideTouchable = false
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val closeButton = popupView.findViewById<ImageButton>(R.id.btnAtras_tus_metas)
        closeButton.setOnClickListener { dismissPopup() }

        popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, 0, 0)
        currentPopupWindow = popupWindow
    }

    fun dismissPopup() {
        currentPopupWindow?.dismiss()
        currentPopupWindow = null
    }

    private fun setupRecyclerView(popupView: View) {
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerViewItemsMetas)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = ItemAdapterMetas(items)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : ItemAdapterMetas.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = items[position]
                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina", "acceder_meta")
                    putExtra("nombre_meta", item.nombreMeta)
                    putExtra("cantidad_en_meta", item.cantidadEnMeta)
                    putExtra("cantidad_final_meta", item.cantidadFinalMeta)
                    putExtra("fecha_inicio_meta", item.fechaInicioMeta)
                    putExtra("numeroDeCuenta" , cuenta)
                }
                context.startActivity(intent)
            }
        })
    }





companion object {
    private fun generateSampleData(cuenta: String , context: Context): List<MetaItem> {
        val bdHelper = DatabaseHelper(context)
        return bdHelper.obtenerMetasEnProceso(cuenta)
       /* return listOf(
            MetaItem("Coco Cocada", "999999", "2000000", "20/08/2023"),
            MetaItem("Bolsillo 2", "1000", "4000", "20/08/2023"),
            MetaItem("Bolsillo 3", "3700", "6000", "20/08/2023"),
            MetaItem("Bolsillo 4", "600", "8000", "20/08/2023"),
            MetaItem("Bolsillo 5", "1000", "10000", "20/08/2023"),
            MetaItem("Bolsillo 6", "8000", "12000", "20/08/2023"),
            MetaItem("Bolsillo 7", "7500", "14000", "20/08/2023"),
            MetaItem("Bolsillo 8", "4900", "16000", "20/08/2023"),
            MetaItem("Bolsillo 9", "1000", "18000", "20/08/2023"),
            MetaItem("Bolsillo 10", "10000", "20000", "20/08/2023")
        )*/
    }
}
}
