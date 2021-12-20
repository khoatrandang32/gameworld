package com.kflower.gameworld.ui.main.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AvatarAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.dialog.ChooseAvatarDialog

import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import com.kflower.gameworld.adapter.CarouselAdapter
import kotlin.math.pow


class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    lateinit var binding: HomeFragmentBinding;
    lateinit var listAvatar: ArrayList<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= HomeFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.apply {
            listAvatar= ArrayList();
            listAvatar.add("https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg")
            listAvatar.add("https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg")
            listAvatar.add("https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg")
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            carousel.layoutManager = layoutManager
            carousel.adapter= CarouselAdapter(requireContext(),listAvatar)
            carousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                   carousel.apply {
                       post {
                           (0 until childCount).forEach { position ->
                               val child = getChildAt(position)
                               val childCenterX = (child.left + child.right) / 2
                               val scaleValue = getGaussianScale(childCenterX, 0.8f, 0.2f, 150.toDouble(),left,right)
                               child.scaleX = scaleValue
                               child.scaleY = scaleValue
                           }
                       }
                   }
                }
            })
        }
    }
    private fun getGaussianScale(
        childCenterX: Int,
        minScaleOffest: Float,
        scaleFactor: Float,
        spreadFactor: Double,
        left:Int,
        right:Int
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

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }
}