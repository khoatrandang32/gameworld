package com.kflower.gameworld.model

import com.google.gson.annotations.SerializedName

data class AudioGroup(
    @SerializedName("_id")
    var id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("listAudio")
    var listAudio: MutableList<AudioBook>)
