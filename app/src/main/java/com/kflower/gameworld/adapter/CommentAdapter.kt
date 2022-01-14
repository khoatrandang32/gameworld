package com.kflower.gameworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kflower.gameworld.R
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.Comment

class CommentAdapter (private val context: Context, var listAudio: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentAdapterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): CommentAdapterHolder {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.audio_item, parent, false)
        val view: View = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        return CommentAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapterHolder, position: Int) {
        holder.bindData(context, listAudio?.get(position))
    }

    override fun getItemCount(): Int {
        return listAudio.size;
    }

    inner class CommentAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtComment: TextView= itemView.findViewById(R.id.txtComment);
        var txtUsername: TextView= itemView.findViewById(R.id.txtUsername);
        var imgAvatar: ImageView= itemView.findViewById(R.id.imgAvatar);
        fun bindData(context: Context, item: Comment) {
            txtComment.text= item.title;
            txtUsername.text= item.writer.fullname;
            Glide.with(context).load(item.writer.avatar).into(imgAvatar);
        }
    }
}