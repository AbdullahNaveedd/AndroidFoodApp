package com.example.foodapp.screens.Profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R

class Profile_Adapter(
    private var items: List<Profile_dataClass>,
    private val onItemClick: (Profile_dataClass) -> Unit
) : RecyclerView.Adapter<Profile_Adapter.InViewHolder>() {

    inner class InViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)

        fun bind(item: Profile_dataClass) {
            item.ImageResId?.let { imageView.setImageResource(it) }
            title.text = item.title ?: ""

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_items, parent, false)
        return InViewHolder(view)
    }

    override fun onBindViewHolder(holder: InViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<Profile_dataClass>) {
        items = newItems
        notifyDataSetChanged()
    }
}
