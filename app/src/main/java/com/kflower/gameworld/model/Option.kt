package com.kflower.gameworld.model

import com.kflower.gameworld.common.core.BaseFragment

data class Option(
    var id: Int,
    var title: String,
    var iconDrawable: Int,
    var fragment:BaseFragment?=null
    )