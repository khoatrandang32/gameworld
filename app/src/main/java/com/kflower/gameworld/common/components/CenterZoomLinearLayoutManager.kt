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
        val childHeight = getChildAt(childCount - 1)!!.height
        val childO = getChildAt(0) as View
        var scaleChild0 =  ((childHeight + getDecoratedTop(childO)).toFloat())/childHeight
        childO.scaleX = scaleChild0
        childO.scaleY = scaleChild0
        childO.alpha= scaleChild0-(1-scaleChild0)
        childO.translationY=-(getDecoratedTop(childO)/3)/scaleChild0
        for (i in 1 until childCount) {
            val child = getChildAt(i) as View
            child.alpha= 1f
            child.scaleX = 1f
            child.scaleY = 1f
            child.translationY=0f

//            val child = getChildAt(i) as View
//            child.translationY= -(child.height/2).toFloat()

//            val d = Math.min(d1, (midpoint - (getDecoratedTop(child) + getDecoratedBottom(child)) / 2f))
//            val scale = 1f - mShrinkAmount * d / d1
//            Log.d("KHOA", "scaleChildren $i: "+scale)

//            child.alpha= scale
        }
    }
}