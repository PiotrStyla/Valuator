package pl.aplikacje.valuator.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.aplikacje.valuator.R
import pl.aplikacje.valuator.database.CarPhotoInDatabase

class ItemListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>()

{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<CarPhotoInDatabase>() // Cached copy of words

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemItemView1: TextView = itemView.findViewById(R.id.text_view_1)
        val itemItemView2: TextView = itemView.findViewById(R.id.text_view_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.fragment_history, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = items[position]
        holder.itemItemView1.text = current.make_name
        holder.itemItemView2.text = current.model_name
    }

    internal fun setItems(items: List<CarPhotoInDatabase>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}