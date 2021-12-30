package com.kflower.gameworld.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.CategoryItemBinding
import com.kflower.gameworld.model.Category
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.roundToInt

class CategoriesAdapter(context: Context, audioGroupList: MutableList<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var context: Context = context;
    private var searchList: MutableList<Category> = audioGroupList;

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
        holder.binding.apply {
            var arrayColor= arrayListOf<String>()
            arrayColor.add("#e9d985")
            arrayColor.add("#b2bd7e")
            arrayColor.add("#749C75")
            arrayColor.add("#6A5D7B")
            arrayColor.add("#5D4A66")
            arrayColor.add("#DEF6CA")
            arrayColor.add("#F8BDC4")
            arrayColor.add("#F497DA")
            arrayColor.add("#F679E5")
            arrayColor.add("#F65BE3")
            val randomNum: Int = ThreadLocalRandom.current().nextInt(0, arrayColor.size)
            container.setBackgroundColor(Color.parseColor(arrayColor[randomNum]))
            txtText.text=position.toString()
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }


}