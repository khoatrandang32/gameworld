package com.kflower.gameworld.common.components

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.log

class CenterZoomLinearLayoutManager(
    context: Context,
) : LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleChildren()
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return if (orientation == VERTICAL) {
            return super.scrollVerticallyBy(dy, recycler, state).also { scaleChildren() }
        } else {
            0
        }
    }

    private fun scaleChildren() {
        val midpoint = height / 2f
        val childHeight = getChildAt(childCount - 1)!!.height
        val childO = getChildAt(0) as View

        for (i in 0 until childCount) {
            val child = getChildAt(i) as View

            if (i == 0) {
                var scale =  ((childHeight + getDecoratedTop(child)).toFloat())/childHeight

                child.scaleX = scale
                child.scaleY = scale
            }
            else{
                child.scaleX = 1f
                child.scaleY = 1f
            }
//            val child = getChildAt(i) as View
//            child.translationY= -(child.height/2).toFloat()

//            val d = Math.min(d1, (midpoint - (getDecoratedTop(child) + getDecoratedBottom(child)) / 2f))
//            val scale = 1f - mShrinkAmount * d / d1
//            Log.d("KHOA", "scaleChildren $i: "+scale)

//            child.alpha= scale
        }
    }
}