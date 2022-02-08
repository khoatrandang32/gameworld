package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.TimeItemBinding
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService
import com.kflower.gameworld.dialog.NewTimeEnterDialog


class TimeListAdapter(
    private val context: Context,
    var listTime: MutableList<Int>,
    var listener: TimeClickListener
) :
    RecyclerView.Adapter<TimeListAdapter.TimeListAdapterHolder>() {
    private var layoutInflater: LayoutInflater? = null
    public var selectedIndex = -1;

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TimeListAdapterHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: TimeItemBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.time_item, parent, false)
        return TimeListAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeListAdapterHolder, position: Int) {
        holder.bindData(context, listTime?.get(position), position)
    }

    override fun getItemCount(): Int {
        return listTime.size;
    }

    inner class TimeListAdapterHolder(itemView: TimeItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding: TimeItemBinding = itemView

        fun bindData(context: Context, item: Int, position: Int) {

            binding?.apply {
                txtTime.text = "$item phút";
                if(item>0){
                    txtTime.text = "$item phút";
                }
                else{
                    txtTime.text = "Khác";
                }

                if(position==selectedIndex){
                    container.setBackgroundColor(context.resources.getColor(R.color.sub_color))
                }
                else{
                    container.setBackgroundColor(context.resources.getColor(R.color.background_color))
                }

                container.setOnClickListener {
                    if(item>0){
                        selectedIndex= position
                        notifyDataSetChanged()
                        listener.onClick(item)
                    }
                    else{
                        var dialog = NewTimeEnterDialog(context, object : TimeClickListener{
                            override fun onClick(item: Int) {

                            }

                        });
                        dialog.show()
                    }
                }

            }
        }
    }

    interface TimeClickListener {
        fun onClick(item: Int)
    }
}