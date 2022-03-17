package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.AudioEpItemBinding
import com.kflower.gameworld.databinding.AudioGroupItemBinding

class AudioEpAdapter(
    private val context: Context,
    var listAudio: MutableList<String>,
    var listener: AudioEpListener
) :
    RecyclerView.Adapter<AudioEpAdapter.AudioEpAudioHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AudioEpAudioHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: AudioEpItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.audio_ep_item, parent, false)
        return AudioEpAudioHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioEpAudioHolder, position: Int) {
        holder.bindData(context, listAudio?.get(position), position)
    }

    override fun getItemCount(): Int {
        return listAudio.size;
    }

    inner class AudioEpAudioHolder(itemView: AudioEpItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: AudioEpItemBinding = itemView

        fun bindData(context: Context, audio: String, position: Int) {
          binding?.apply {
              txtAudioEp.text = "Ep ${position+1}"
              isSelect= mediaPlayer.currentMediaItemIndex==position

              if(mediaPlayer.currentMediaItemIndex==position){
                  imgDownload.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
              }
              else{
                  imgDownload.setColorFilter(ContextCompat.getColor(context, R.color.main_color), android.graphics.PorterDuff.Mode.SRC_IN);
              }
              container.setOnClickListener {
                  listener.onClick(audio,position);
                  notifyDataSetChanged()
              }
              imgDownload.setOnClickListener {
                  listener.onDownload(audio,position)
              }
          }

        }
    }

    interface AudioEpListener {
        fun onClick(audioUrl: String,position: Int)
        fun onDownload(audioUrl: String,position: Int)
    }
}