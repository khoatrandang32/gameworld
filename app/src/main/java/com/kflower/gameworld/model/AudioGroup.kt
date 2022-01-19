package com.kflower.gameworld.model

import com.google.gson.annotations.SerializedName

data class AudioGroup(
    @SerializedName("title")
    var title: String,
    @SerializedName("listAudio")
    var listAudio: MutableList<AudioBook>)
