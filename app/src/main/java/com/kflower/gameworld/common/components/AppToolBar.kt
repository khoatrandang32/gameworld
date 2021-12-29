package com.kflower.gameworld.common.components

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.kflower.gameworld.R

class AppToolBar : LinearLayout {
    private lateinit var safeView: View
    private lateinit var imgLeft: ImageView
    private lateinit var imgRight: ImageView
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
            R.layout.app_tool_bar_layout, null
        )
        view.layoutParams = layoutParams
        this.addView(view)

         imgLeft = view.findViewById(R.id.imgLeft)
         imgRight = view.findViewById(R.id.imgRight)
    }

    fun setOnClickLeft(listener: OnClickListener){
        imgLeft.setOnClickListener(listener)

    }
    fun setOnClickRight(listener: OnClickListener){
        imgRight.setOnClickListener(listener)
    }



}