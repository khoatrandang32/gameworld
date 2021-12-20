package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.R

class CarouselAdapter (
    var context: Context,
    var list: MutableList<String>
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bindData(context, list?.get(position))
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CarouselViewHolder(inflater.inflate(R.layout.carousel_item, parent, false))
    }

    class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView: ImageView = itemView.findViewById(R.id.carouselImage)

        fun bindData(context: Context, image: String) {
            Glide.with(context).load(image).into(imgView);

        }
    }
}