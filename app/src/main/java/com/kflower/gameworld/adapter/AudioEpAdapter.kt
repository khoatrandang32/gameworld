package com.kflower.gameworld.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
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
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.downloadTable
import com.kflower.gameworld.MyApplication.Companion.fetchAudio
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.R
import com.kflower.gameworld.common.getAudioEpFromUri
import com.kflower.gameworld.databinding.AudioEpItemBinding
import com.kflower.gameworld.databinding.AudioGroupItemBinding
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.model.AudioBook
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Status

class AudioEpAdapter(
    private val context: Context,
    var listAudio: MutableList<String>,
    var listener: AudioEpListener,
    var audioItem: AudioBook,
    var downloadMode: Boolean? = false
) :
    RecyclerView.Adapter<AudioEpAdapter.AudioEpAudioHolder>() {
    private var layoutInflater: LayoutInflater? = null
    var listDownload = mutableListOf<Int>()


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

    inner class AudioEpAudioHolder(itemView: AudioEpItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding: AudioEpItemBinding = itemView

        fun bindData(context: Context, audio: String, position: Int) {

            binding?.apply {
                txtAudioEp.text = "Tập ${position + 1}"
                if (downloadMode == false) {
                    isSelect = mediaPlayer.currentMediaItemIndex == position
                    container.setOnClickListener {
                        listener.onClick(audio, position);
                        notifyDataSetChanged()
                    }
                } else {
                    var data = downloadTable.findDownloadByAudioId(audioItem.id, position + 1)

                    binding.isDisable =
                        data?.state == DownloadState.COMPLETED || data?.state == DownloadState.DOWNLOADING
                    binding.container.isEnabled =
                        !(data?.state == DownloadState.COMPLETED || data?.state == DownloadState.DOWNLOADING)

                    if (data?.state == DownloadState.DOWNLOADING) {
                        layoutProgress.visibility=View.VISIBLE
                        fetchAudio.getDownload(data.id.toInt()) {
                            it?.apply {
                                layoutProgress.progress= progress
                                if(status!=Status.DOWNLOADING){
                                    layoutProgress.visibility= View.GONE
                                }
                            }
                        }
                    }
                    else layoutProgress.visibility= View.GONE

                    isSelect = listDownload.contains(position)
                    container.setOnClickListener {
                        if (listDownload.contains(position)) {
                            listDownload.remove(position);
                        } else {
                            listDownload.add(position);
                        }
                        notifyItemChanged(position)
                        listener.onClick(audio, position);

                    }
                }
            }

        }
    }

    fun updateDownload(download: Download) {
        var ep = download.fileUri.toString().getAudioEpFromUri(context as Activity)
        notifyItemChanged(ep-1)
    }

    interface AudioEpListener {
        fun onClick(audioUrl: String, position: Int)
    }
}