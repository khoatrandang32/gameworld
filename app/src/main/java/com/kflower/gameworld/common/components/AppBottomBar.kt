package com.kflower.gameworld.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.kflower.gameworld.R

class AppBottomBar:LinearLayout {
    constructor(context: Context) : super(context) {
        initView(null)
    }



    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }
    private fun initView(attrs: AttributeSet?) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.app_bottom_bar_layout, null
        )
        view.layoutParams = layoutParams
        this.addView(view)
    }
}