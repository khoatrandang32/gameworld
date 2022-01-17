package com.kflower.gameworld.common.components

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.kflower.gameworld.R

class AppLinearLayout : LinearLayout {
    private lateinit var safeView: View
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
//        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.app_linear_layout, null
        )
//        view.layoutParams = layoutParams
        safeView = view.findViewById(R.id.safeView);

        val rectangle = Rect()
        val window: Window = (context as Activity).window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight: Int = rectangle.top
        safeView.layoutParams= LayoutParams(safeView.width,statusBarHeight);

        this.addView(view)

    }
    fun setStatusBarColor(color:Int){
        safeView.setBackgroundColor(color)
    }
    fun setStatusBarBackground(background:Drawable){
        safeView.background=background
    }

}