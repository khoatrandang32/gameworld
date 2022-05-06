package com.kflower.gameworld.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.R
import com.kflower.gameworld.common.toBitMap
import com.kflower.gameworld.databinding.DownloadingItemBinding
import com.kflower.gameworld.model.DownloadAudio
import glimpse.glide.GlimpseTransformation

class DownloadingAudioAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<DownloadingAudioAdapter.DownloadingAudioAdapterViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var listDownload = mutableListOf<DownloadAudio>()
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DownloadingAudioAdapterViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: DownloadingItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.downloading_item, parent, false)
        return DownloadingAudioAdapterViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: DownloadingAudioAdapterViewHolder, position: Int) {
        holder.bindData(context, listDownload?.get(position), position)
    }

    override fun getItemCount(): Int {
        return listDownload.size;
    }

    inner class DownloadingAudioAdapterViewHolder(
        itemView: DownloadingItemBinding,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(itemView.root) {
        val binding: DownloadingItemBinding = itemView
        val parent: ViewGroup = parent

        fun bindData(context: Context, downloadAudio: DownloadAudio, position: Int) {
            var result = MyApplication.audioTable.findAudio(downloadAudio.audioId)
            if (result.size > 0) {
                var item = result[0]
                binding?.apply {

                    if (imgAudio.drawable == null) {
                        Glide.with(context).load(item.imgBase64.toBitMap())
                            .transform(GlimpseTransformation())
                            .into(imgAudio);
                    }
                    txtAudioBookName.text = item.title + " - Tập ${downloadAudio.ep}"
                    if(downloadAudio.progress>0){
                        txtProgress.text = "${downloadAudio.progress}%";
                    }
                    else{
                        txtProgress.text = "0%";
                    }
                    progressBar.progress = downloadAudio.progress

                }
            }


        }
    }

    fun updateProgress(id: String, progress: Int) {
        listDownload.mapIndexed { index, downloadAudio ->
            if(downloadAudio.id==id){
                downloadAudio.progress=progress
            }
        }
    }

    fun setListData(listDownload: MutableList<DownloadAudio>) {
        this.listDownload = listDownload;
        notifyDataSetChanged()

    }

    fun addDownload(item: DownloadAudio) {
        var isContain= false;

        listDownload.forEach {
            if(it.id==item.id){
                isContain=true
                return@forEach
            }
        }
        if(!isContain){
            this.listDownload.add(item);
        }
    }

    fun removeDownload(item: DownloadAudio) {
        this.listDownload.mapIndexed { index, downloadAudio ->
            if(downloadAudio.id==item.id){
                listDownload.removeAt(index)
                return@mapIndexed
            }
        }
    }

}