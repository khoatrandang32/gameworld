package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.OptionItemBinding
import com.kflower.gameworld.model.Option

class OptionAdapter(
    private val context: Context,
    var listOption: MutableList<Option>,
    var listener: ClickOptionListener
) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): OptionViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: OptionItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.option_item, parent, false)
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bindData(context, listOption?.get(position), position)
    }

    override fun getItemCount(): Int {
        return listOption.size;
    }

    inner class OptionViewHolder(itemView: OptionItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding: OptionItemBinding = itemView

        fun bindData(context: Context, option: Option, position: Int) {
            binding?.apply {
                txtName.text= option.title;
                imgIcon.setImageDrawable(context.getDrawable(option.iconDrawable))
            }
        }
    }

    interface ClickOptionListener {
        fun onClick(audioUrl: Option, position: Int)
    }
}