package com.kflower.gameworld.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("title")
    var title: String,
    @SerializedName("imgThumbnail")
    var img: String,
    @SerializedName("_id")
    var id: String?
)
