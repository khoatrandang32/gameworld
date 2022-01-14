package com.kflower.gameworld.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AudioBook(
    @SerializedName("_id")
    var id: String,
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String,
    @SerializedName("episodes")
    var episodes: MutableList<String>,
    @SerializedName("title")
    var title: String,
    @SerializedName("decription")
    var decription: String,
    @SerializedName("reader")
    var reader: String,
    @SerializedName("author")
    var author: String,
    @SerializedName("categories")
    var categories: MutableList<Category>,
    @SerializedName("episodesAmount")
    var episodesAmount: Int,
    @SerializedName("comments")
    var comments: MutableList<Comment>,
)
