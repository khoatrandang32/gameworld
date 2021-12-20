package com.kflower.gameworld.common.components

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color.blue
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.R
import kotlin.math.pow

class Carousel(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private val activeColor
            by lazy { ContextCompat.getColor(context, R.color.main_color) }
    private val inactiveColor
            by lazy { ContextCompat.getColor(context, R.color.black) }
    private var viewsToChangeColor = listOf<Int>()

    override fun setAdapter(newAdapter: Adapter<*>?) {
        super.setAdapter(newAdapter)
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        newAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                post {
                    val sidePadding = (width / 2) - (getChildAt(0).width / 2)
                    setPadding(sidePadding, 0, sidePadding, 0)
                    scrollToPosition(0)
                    addOnScrollListener(object : OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            onScrollChanged();
                        }
                    })
                }
            }
        })
        adapter = newAdapter
    }
    private fun onScrollChanged() {
        post {
            (0 until childCount).forEach { position ->
                val child = getChildAt(position)
                val childCenterX = (child.left + child.right) / 2
                val scaleValue = getGaussianScale(childCenterX, 1f, 1f, 150.toDouble())
                child.scaleX = scaleValue
                child.scaleY = scaleValue
            }
        }
    }
    private fun getGaussianScale(
        childCenterX: Int,
        minScaleOffest: Float,
        scaleFactor: Float,
        spreadFactor: Double
    ): Float {
        val recyclerCenterX = (left + right) / 2
        return (Math.pow(
            Math.E,
            -(childCenterX - recyclerCenterX.toDouble()).pow(2.toDouble()) / (2 * Math.pow(
                spreadFactor,
                2.toDouble()
            ))
        ) * scaleFactor + minScaleOffest).toFloat()
    }


}