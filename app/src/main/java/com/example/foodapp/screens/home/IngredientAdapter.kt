package com.example.foodapp.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R

class IngredientAdapter(private var items: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.InViewHolder>() {

    inner class InViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val title: TextView = itemView.findViewById(R.id.title)

        fun bind(item: Ingredient, context:Context) {

            Glide.with(context).load(item.icon ?: "").into(imageView)

//            item.icon?.let { imageView.setImageResource(it) }
            title.text = item.title ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_ingredient, parent, false)
        return InViewHolder(view)
    }

    override fun onBindViewHolder(holder: InViewHolder, position: Int) {
        holder.bind(items[position],holder.itemView.context)
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<Ingredient>) {
        items = newItems
        notifyDataSetChanged()
    }
}
