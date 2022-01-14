package com.kflower.gameworld.model

import com.google.gson.annotations.SerializedName
import com.kflower.gameworld.User

data  class Comment(
    @SerializedName("_id")
    var id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("writer")
    var writer: User    ,
    @SerializedName("createAt")
    var createAt: String,

)