package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.CategoryItemBinding
import com.kflower.gameworld.model.Category

class CategoriesAdapter(context: Context, audioGroupList: MutableList<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var categoryList: MutableList<Category> = audioGroupList;

    inner class CategoriesAdapterHolder(itemBinding: CategoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: CategoryItemBinding = itemBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: CategoryItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.category_item, parent, false)
        return CategoriesAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesAdapterHolder, position: Int) {
        val curCategory= categoryList[position];
        holder.binding.apply {
            txtText.text=curCategory.title
            Glide.with(context).load(curCategory.img).into(imgBg);
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


}