package com.example.foodapp.screens.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R

class MenuAdapter(private var items: List<menuDataClass>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val details: TextView = itemView.findViewById(R.id.details)
        val price: TextView = itemView.findViewById(R.id.price)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.name
        holder.details.text = item.details
        holder.price.text = "$ ${item.price ?: 0}"

        Glide.with(holder.imageView.context)
            .load(item.imageUrl)
            .into(holder.imageView)
        }

    override fun getItemCount() = items.size

    fun submitList(newList: List<menuDataClass>) {
        items = newList
        notifyDataSetChanged()
    }
}
