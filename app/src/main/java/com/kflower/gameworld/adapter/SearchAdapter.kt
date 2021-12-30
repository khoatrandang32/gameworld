package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.SearchItemBinding

class SearchAdapter (context: Context, audioGroupList: MutableList<String>) :
    RecyclerView.Adapter<SearchAdapter.SearchAdapterHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var searchList: MutableList<String> = audioGroupList;

    inner class SearchAdapterHolder(itemBinding: SearchItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: SearchItemBinding = itemBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: SearchItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.search_item, parent, false)
        return SearchAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapterHolder, position: Int) {
        holder.binding.apply {
            txtTitle.text= searchList[position]
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }


}