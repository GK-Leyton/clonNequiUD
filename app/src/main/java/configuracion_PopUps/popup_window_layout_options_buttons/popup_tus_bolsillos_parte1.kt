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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nequi_proyecto_componentes.R


import com.example.nequi_proyecto_componentes.PantallaDeCarga

class PopupHelperTusBolsillosParte1(private val context: Context) {

    private var currentPopupWindow: PopupWindow? = null

    fun showPopup(viewAnchor: View , cuenta : String) {
        if (viewAnchor.windowToken == null) {
            return
        }

        currentPopupWindow?.dismiss()

        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_tus_bolsillos_parte1, null)

        val recyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerViewItems)
        val btnCrearBolsillo = popupView.findViewById<FrameLayout>(R.id.frameCrearBolsillo)
        val btnCrearBolsillo2 = popupView.findViewById<ImageButton>(R.id.btnCrearBolsillo)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)

        val adapter = ItemAdapter(generateSampleData(cuenta))
        recyclerView.adapter = adapter



        btnCrearBolsillo.setOnClickListener{
            currentPopupWindow?.dismiss()
            val popupHelper = PopupHelperCrearBolsillo(context)
            popupHelper.showPopup(btnCrearBolsillo , cuenta)
        }
        btnCrearBolsillo2.setOnClickListener{
            currentPopupWindow?.dismiss()
            val popupHelper = PopupHelperCrearBolsillo(context)
            popupHelper.showPopup(btnCrearBolsillo , cuenta)
        }


        //declaracion del evento onclick de cada item
        // Configura el listener de clics en el adaptador
        adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Obtén el nombre y cantidad del ítem en la posición clickeada
                val item = generateSampleData(cuenta)[position]
                val message = "${item.first} tiene ${item.second}"

                val intent = Intent(context, PantallaDeCarga::class.java).apply {
                    putExtra("proximaPagina" , "acceder_bolsillo")
                    putExtra ("nombre_bolsillo" , item.first)
                    putExtra ("cantidad_en_bolsillo" , item.second)
                    putExtra("numeroDeCuenta" , cuenta)

                }
                context.startActivity(intent)

            }
        })

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

        val closeButton = popupView.findViewById<ImageButton>(R.id.btnAtras_tus_bolsillos)
        closeButton.setOnClickListener {
            dismissPopup()
        }

        popupWindow.showAtLocation(viewAnchor, Gravity.CENTER, 0, 0)
        currentPopupWindow = popupWindow
    }

    fun dismissPopup() {
        currentPopupWindow?.dismiss()
        currentPopupWindow = null
    }

    //funcion para conseguir la informacion
    private fun generateSampleData(cuenta : String): List<Pair<String, String>> {
        val bdHelper = DatabaseHelper(context)
        val res = bdHelper.obtenerBolsillosConMontos(cuenta.toLong())
        return res
        /*return listOf(
            "Coco Cocada" to "$ 999999",
            "Bolsillo 2" to "$ 2000",
            "Bolsillo 3" to "$ 3000",
            "Bolsillo 4" to "$ 4000",
            "Bolsillo 5" to "$ 5000",
            "Bolsillo 6" to "$ 6000",
            "Bolsillo 7" to "$ 7000",
            "Bolsillo 8" to "$ 8000",
            "Bolsillo 5" to "$ 5000",
            "Bolsillo 6" to "$ 6000",
            "Bolsillo 7" to "$ 7000",
            "Bolsillo 8" to "$ 8000",
            "Bolsillo 9" to "$ 9000"
        )*/
    }
}
