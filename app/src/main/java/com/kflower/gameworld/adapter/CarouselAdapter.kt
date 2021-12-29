package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.R
import com.kflower.gameworld.model.AudioBook

class CarouselAdapter (
    var context: Context,
    var list: MutableList<AudioBook>
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bindData(context, list?.get(position % list.size))
    }

    override fun getItemCount(): Int = Integer.MAX_VALUE;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CarouselViewHolder(inflater.inflate(R.layout.carousel_item, parent, false))
    }

    class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView: ImageView = itemView.findViewById(R.id.carouselImage)
        private val txtAudioBookName: TextView = itemView.findViewById(R.id.txtAudioBookName)

        fun bindData(context: Context, audio: AudioBook) {
            Glide.with(context).load(audio.thumbnailUrl).into(imgView);
            txtAudioBookName.text= audio.name;
        }
    }
}