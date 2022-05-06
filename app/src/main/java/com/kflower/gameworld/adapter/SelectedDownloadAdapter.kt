package com.kflower.gameworld.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.R

class SelectedDownloadAdapter (private val context: Context) :
    RecyclerView.Adapter<SelectedDownloadAdapter.ViewHolder>() {

    var listItem= mutableListOf<Int>()


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.small_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(context, listItem?.get(position))
    }

    override fun getItemCount(): Int {
        return listItem.size;
    }

    public fun setData(listData:MutableList<Int>){
        listItem= listData;
        notifyDataSetChanged()
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.txtTitle)
        fun bindData(context: Context, item: Int) {

            Log.d(TAG, "bindData: "+listItem.size)
            textTitle.text= "Tập ${item+1}";
        }
    }
}
