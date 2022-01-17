package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.CategoryItemBinding
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.Category
import glimpse.glide.GlimpseTransformation

class CategoriesAdapter(
    context: Context,
    audioGroupList: MutableList<Category>,
    listener: OnClickCategory
) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var categoryList: MutableList<Category> = audioGroupList;
    private var mListener: OnClickCategory = listener;

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
        val curCategory = categoryList[position];
        holder.binding.apply {
            txtText.text = curCategory.title
            Glide.with(context).load(curCategory.img)
                .transform(GlimpseTransformation())
                .into(imgBg);
            container.setOnClickListener {
                mListener.onClick(categoryList[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface OnClickCategory {
        fun onClick(item: Category)
    }

    fun setData(list: MutableList<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }


}