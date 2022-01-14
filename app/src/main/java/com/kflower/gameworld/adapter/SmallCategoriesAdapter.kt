package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.R
import com.kflower.gameworld.model.Category

class SmallCategoriesAdapter(private val context: Context, var listCategories: MutableList<Category>) :
    RecyclerView.Adapter<SmallCategoriesAdapter.SmallCategoriesAdapterHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SmallCategoriesAdapterHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.small_category_item, parent, false)
        return SmallCategoriesAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: SmallCategoriesAdapterHolder, position: Int) {
        holder.bindData(context, listCategories?.get(position))
    }

    override fun getItemCount(): Int {
        return listCategories.size;
    }

    inner class SmallCategoriesAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.txtTitle)
        fun bindData(context: Context, item: Category) {
            textTitle.text= item.title;
        }
    }
}
