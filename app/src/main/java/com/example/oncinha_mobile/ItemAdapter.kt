package com.example.oncinha_mobile


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loja, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)

        // Set click listener for the item view
        //PAssa a acão
        holder.itemView.setOnClickListener {
            //Log.d("OIOi", "Clicked $item")
            onClickListener?.onClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Set the click listener for the adapter
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: Item)
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.descItem)
        private val precoTextView: TextView = itemView.findViewById(R.id.item_preco)


        fun bind(item: Item) {
            nomeTextView.text = item.nome
            precoTextView.text = item.preco
        }

    }
}
