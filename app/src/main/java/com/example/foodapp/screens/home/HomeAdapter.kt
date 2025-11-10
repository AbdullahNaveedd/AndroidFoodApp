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

class HomeAdapter ( private val items: List<HomeModel>
) : RecyclerView.Adapter<HomeAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)


        fun bind(item: HomeModel, context: Context) {

            Glide.with(context)
                .load(item.imageResId ?: "")
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView)
//            item.imageResId?.let { imageView.setImageResource(it) }
            imageView.clipToOutline=true

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(items[position], holder.itemView.context)
    }

    override fun getItemCount() = items.size
}