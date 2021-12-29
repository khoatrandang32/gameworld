package com.kflower.gameworld.ui.template

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import androidx.core.view.ViewCompat

import com.kflower.gameworld.adapter.CarouselAdapter
import kotlin.math.abs
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioAdapter
import com.kflower.gameworld.model.AudioBook
import java.util.*
import kotlin.collections.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kflower.gameworld.databinding.TemplateFragmentBinding


class TemplateFragment : BaseFragment() {

    companion object {
        fun newInstance() = TemplateFragment()
    }

    private lateinit var viewModel: TemplateViewModel

    lateinit var binding: TemplateFragmentBinding;
    lateinit var dataListAudio: ArrayList<AudioBook>;

    var currentIndex = 0;
    var isScrolling = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("KHOA", "onCreate: ")
        dataListAudio = arrayListOf();
        val dvHeight = Resources.getSystem().displayMetrics.heightPixels;
        val dvWidth = Resources.getSystem().displayMetrics.widthPixels;
        val witdhCard = dvWidth - resources.getDimensionPixelOffset(R.dimen.dimen_16) * 3;
        binding = TemplateFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(TemplateViewModel::class.java)
        //
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                if (isScrolling) {
                    isScrolling = false
                } else {
                    binding.viewpager.currentItem = currentIndex;
                }
            }
        }
        val timer = Timer()
        //
        binding.apply {
            Glide.with(requireContext())
                .load("https://i.pinimg.com/originals/bc/d5/c9/bcd5c9519581acc60bd60a429ab0c88f.jpg")
                .into(imgTopAudio);

            dataListAudio.add(
                AudioBook(
                    "https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg",
                    "",
                    "Audio Book 1"
                )
            )
            dataListAudio.add(
                AudioBook(
                    "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg",
                    "",
                    "Audio Book 2"
                )
            )
            dataListAudio.add(
                AudioBook(
                    "https://i.pinimg.com/originals/bc/d5/c9/bcd5c9519581acc60bd60a429ab0c88f.jpg",
                    "",
                    "Audio Book 3"
                )
            )
            dataListAudio.add(
                AudioBook(
                    "https://cdn-amz.fadoglobal.io/images/I/71OIhbUOF-L.jpg",
                    "",
                    "Audio Book 4"
                )
            )
            viewpager.apply {

                orientation = ViewPager2.ORIENTATION_HORIZONTAL;

                adapter = CarouselAdapter(context, dataListAudio);
                offscreenPageLimit = 3;

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        currentIndex = position + 1;
                    }
                });


                setPageTransformer { page, position ->
                    isScrolling = true;
                    if (position < -1) {
                        page.z = position * 100
                    } else {
                        page.z = -position * 100
                    }
                    if (orientation === ViewPager2.ORIENTATION_HORIZONTAL) {
                        if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            page.translationX = -position
                        } else {
                            if (position >= 0) {
                                page.translationX = position * -(witdhCard - (position * 40f))
                                page.scaleX = (witdhCard - (position * 94f)) / witdhCard
                                page.scaleY = (witdhCard - (position * 94f)) / witdhCard
                                page.alpha =
                                    ((dataListAudio.size - abs(position)) / dataListAudio.size) - position * 0.2f
                            } else {
                                page.translationX = position * 16f
                            }
                        }
                    } else {
                        page.translationY = position
                    }
                }
            }

            timer.schedule(task, Date(), 2500)
            //


            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val layoutManagerTop =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            listViewSuggestion.layoutManager = layoutManager
            listViewTop.layoutManager = layoutManagerTop
            listViewTop.adapter = AudioAdapter(requireContext(), dataListAudio)
            listViewSuggestion.adapter = AudioAdapter(requireContext(), dataListAudio)
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }
}