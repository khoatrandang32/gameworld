package com.kflower.gameworld.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AudioBook(
    @SerializedName("_id")
    var id: String,
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String,
    @SerializedName("baseEpisode")
    var baseEpisode: String,
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
    var comments: MutableList<Comment>?,
    //
    var rate: Int =0,
    var imgBase64: String,
    var progress: Long,
    var curEp: Int=0,
    var historyTime: Int=0,
)
