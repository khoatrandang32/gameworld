package com.kflower.gameworld.common.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import kotlin.math.log

class AppEdt : AppCompatEditText {

    private var listener: OnKeyPreImeListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("KHOAxxx", "onKeyPreIme: ")
        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            listener?.onImeBack(this)
        }
        return super.dispatchKeyEvent(event)
    }

    interface OnKeyPreImeListener {
        fun onImeBack(editText: AppEdt)
    }
    fun setOnKeyPreImeListener(listener: OnKeyPreImeListener){
        this.listener=listener
    }

}