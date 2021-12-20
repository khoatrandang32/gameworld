package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.R


class AvatarAdapter(
    var context: Context,
    var list: MutableList<Int>
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {
    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bindData(context, list?.get(position))
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AvatarViewHolder(inflater.inflate(R.layout.avatar_item, parent, false))
    }


    class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView: ImageView = itemView.findViewById(R.id.imgAvatar)

        fun bindData(context: Context, image: Int) {
            imgView.setImageDrawable(context.getDrawable(image));
        }
    }
}