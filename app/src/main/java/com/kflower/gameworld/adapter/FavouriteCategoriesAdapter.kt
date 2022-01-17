package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.FavCategoryItemBinding
import com.kflower.gameworld.model.Category
import glimpse.glide.GlimpseTransformation

class FavouriteCategoriesAdapter(context: Context, audioGroupList: MutableList<Category>) :
    RecyclerView.Adapter<FavouriteCategoriesAdapter.FavouriteCategoriesAdapterHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var categoryList: MutableList<Category> = audioGroupList;

    inner class FavouriteCategoriesAdapterHolder(itemBinding: FavCategoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: FavCategoryItemBinding = itemBinding

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteCategoriesAdapterHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: FavCategoryItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.fav_category_item, parent, false)
        return FavouriteCategoriesAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteCategoriesAdapterHolder, position: Int) {
        val curCategory = categoryList[position];
        holder.binding.apply {
            txtText.text = curCategory.title
            Glide.with(context).load(curCategory.img)
                .transform(GlimpseTransformation()).into(imgBg);
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setData(list: MutableList<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }


}