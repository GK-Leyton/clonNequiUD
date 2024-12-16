package configuracion_PopUps.popup_window_layout_options_buttons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nequi_proyecto_componentes.R

data class MetaItem(
    val nombreMeta: String,
    val cantidadEnMeta: String,
    val cantidadFinalMeta: String,
    val fechaInicioMeta: String
)

class ItemAdapterMetas(private val items: List<MetaItem>) :
    RecyclerView.Adapter<ItemAdapterMetas.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_meta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtNombreMeta.text = item.nombreMeta
        holder.txtCantidadEnMeta.text = "$"+item.cantidadEnMeta
        holder.txtCantidadFinalMeta.text = "$"+item.cantidadFinalMeta

        // Extraer valores numéricos de las cadenas
        val cantidadEnMeta = item.cantidadEnMeta.toIntOrNull() ?: 0
        val cantidadFinalMeta = item.cantidadFinalMeta.toIntOrNull() ?: 1

        // Calcular el progreso
        val progreso = if (cantidadFinalMeta > 0) {
            (cantidadEnMeta.toFloat() / cantidadFinalMeta * 100).toInt()
        } else {
            0
        }

        // Establecer el progreso de la ProgressBar
        holder.progressBarMeta.progress = progreso

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }


    override fun getItemCount() = items.size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombreMeta: TextView = itemView.findViewById(R.id.txtNombreMeta)
        val txtCantidadEnMeta: TextView = itemView.findViewById(R.id.txtCantidadEnMeta)
        val txtCantidadFinalMeta: TextView = itemView.findViewById(R.id.txtCantidadFinalMeta)
        val progressBarMeta: ProgressBar = itemView.findViewById(R.id.progressBarMeta)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
