package pl.aplikacje.valuator.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_value_page.view.*
import kotlinx.android.synthetic.main.item_view.view.*
import pl.aplikacje.valuator.R
import pl.aplikacje.valuator.database.CarPhotoInDatabase
import pl.aplikacje.valuator.databinding.FragmentHistoryBinding
import java.util.concurrent.ThreadLocalRandom.current

class ItemListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>()



{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<CarPhotoInDatabase>() // Cached copy of cars


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemItemView1: TextView  = itemView.findViewById(R.id.text_view_1)
        val itemItemView2: TextView = itemView.findViewById(R.id.text_view_2)
        val itemItemView3: TextView = itemView.findViewById(R.id.text_view_3)
        val itemItemView4: TextView = itemView.findViewById(R.id.text_view_4)
        val itemItemView5: ImageView = itemView.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = items[position]
        holder.itemItemView1.text = current.make_name
        holder.itemItemView2.text = current.model_name
        holder.itemItemView3.text = current.years
        holder.itemItemView4.text = current.savedUri
        val rawTakenImage = BitmapFactory.decodeFile(current.savedUri?.toString())
        holder.itemItemView5.image_view.setImageBitmap(rawTakenImage)




    }



    internal fun setItems(items: List<CarPhotoInDatabase>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}