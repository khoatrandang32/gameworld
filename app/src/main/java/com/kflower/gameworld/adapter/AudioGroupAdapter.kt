package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.AudioGroupItemBinding

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.model.AudioGroup


class AudioGroupAdapter(context: Context, audioGroupList: MutableList<AudioGroup>) :
    RecyclerView.Adapter<AudioGroupAdapter.MyViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var audioGroupList: MutableList<AudioGroup> = audioGroupList;

    inner class MyViewHolder(itemBinding: AudioGroupItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: AudioGroupItemBinding = itemBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: AudioGroupItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.audio_group_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val layoutManagerVertical =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val curItem = audioGroupList[position]
        holder.binding.apply {
            groupName = curItem.title;
            lvVerticalAudio.layoutManager = layoutManagerVertical;
            lvVerticalAudio.adapter = AudioVerticalAdapter(context, curItem.listAudio);
        }
    }

    override fun getItemCount(): Int {
        return audioGroupList.size
    }


}