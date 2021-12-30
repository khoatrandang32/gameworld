package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.SearchGroupItemBinding
import com.kflower.gameworld.model.SearchGroup

class GroupSearchAdapter (context: Context, audioGroupList: MutableList<SearchGroup>) :
    RecyclerView.Adapter<GroupSearchAdapter.GroupSearchHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var searchGrList: MutableList<SearchGroup> = audioGroupList;

    inner class GroupSearchHolder(itemBinding: SearchGroupItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: SearchGroupItemBinding = itemBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupSearchHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: SearchGroupItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.search_group_item, parent, false)
        return GroupSearchHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupSearchHolder, position: Int) {
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        val curItem = searchGrList[position]
        holder.binding.apply {
            txtTitle.text= curItem.title
            lvSearch.layoutManager=layoutManager;
            lvSearch.adapter= SearchAdapter(context,curItem.listSearch)
        }
    }

    override fun getItemCount(): Int {
        return searchGrList.size
    }


}