package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.kflower.gameworld.R
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook

class AudioVerticalAdapter(
    private val context: Context,
    var listAudio: MutableList<AudioBook>,
    val listener: OnClickAudioBook
) :
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

    fun setData(list: MutableList<AudioBook>){
        listAudio= list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listAudio.size;
    }

    inner class AudioVerticalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView: ImageView = itemView.findViewById(R.id.imageAudio)
        private val txtAudioBookName: TextView = itemView.findViewById(R.id.txtAudioBookName)
        private val txtEpisodesAmount: TextView = itemView.findViewById(R.id.txtEpisodesAmount)
        private val container: LinearLayout = itemView.findViewById(R.id.container)
        private val lvCategories: RecyclerView = itemView.findViewById(R.id.lvCategories)

        fun bindData(context: Context, audio: AudioBook) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            //
            lvCategories.layoutManager = layoutManager;
            lvCategories.adapter = SmallCategoriesAdapter(context, audio.categories)

            Glide.with(context).load(audio.thumbnailUrl).into(imgView);
            txtAudioBookName.text = audio.title
            txtEpisodesAmount.text = "${audio.episodesAmount} Episodes";
            container.setOnClickListener {
                listener.onClick(audio)
            }
        }
    }
}