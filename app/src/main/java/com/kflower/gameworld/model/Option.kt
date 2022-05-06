package com.kflower.gameworld.model

import androidx.fragment.app.Fragment

data class Option(
    var id: Int,
    var title: String,
    var iconDrawable: Int,
    var fragment:Fragment?=null
    )