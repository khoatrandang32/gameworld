package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.AvatarItemBinding

class AvatarAdapter: BaseAdapter{
    var context: Context;
    private var list:MutableList<Int>

    constructor(context: Context,list: MutableList<Int>){
        this.context=context;
        this.list= list;
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.avatar_item, null)
        //
        var imgAvatar=view.findViewById<ShapeableImageView>(R.id.imgAvatar);
        imgAvatar.setImageDrawable(context.getDrawable(list[position]))

        return view;
    }
}