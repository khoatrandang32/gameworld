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

class AudioVerticalAdapter(private val context: Context, var listAudio: MutableList<AudioBook>) :
    RecyclerView.Adapter<AudioVerticalAdapter.AudioVerticalHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AudioVerticalHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.audio_vertical_item, parent, false)
        return AudioVerticalHolder(view)
    }

    override fun onBindViewHolder(holder: AudioVerticalHolder, position: Int) {
        holder.bindData(context, listAudio?.get(position))
    }

    override fun getItemCount(): Int {
        return listAudio.size;
    }

    inner class AudioVerticalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView: ImageView = itemView.findViewById(R.id.imageAudio)
        private val txtAudioBookName: TextView = itemView.findViewById(R.id.txtAudioBookName)

        fun bindData(context: Context, audio: AudioBook) {
            Glide.with(context).load(audio.thumbnailUrl).into(imgView);
            txtAudioBookName.text = audio.name
        }
    }
}