package configuracion_PopUps.popup_window_layout_options_buttons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nequi_proyecto_componentes.R

class ItemAdapter(private val items: List<Pair<String, String>>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    // Define la interfaz para manejar los clics
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // Crea una instancia de OnItemClickListener
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_bolsillo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, amount) = items[position]
        holder.nameTextView.text = name
        holder.amountTextView.text = amount

        // Configura el clic en el ítem
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtNombreBolsillo)
        val amountTextView: TextView = itemView.findViewById(R.id.textView63)
    }
}
