package com.kflower.gameworld.ui.main.home

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioAdapter
import com.kflower.gameworld.adapter.AudioVerticalAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding

import com.kflower.gameworld.model.AudioBook
import kotlin.collections.ArrayList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.kflower.gameworld.adapter.AudioGroupAdapter
import com.kflower.gameworld.dialog.AppDrawer
import com.kflower.gameworld.dialog.ChooseAvatarDialog
import com.kflower.gameworld.model.AudioGroup


class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    lateinit var binding: HomeFragmentBinding;
    lateinit var dataListAudio: ArrayList<AudioBook>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeFragmentBinding.inflate(layoutInflater)
        dataListAudio = arrayListOf();
        initFakeData();
        //
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerVertical =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.apply {
            binding.containerLayout.setStatusBarColor(resources.getColor(R.color.main_color))
            //
            lvAudio.layoutManager = layoutManager;
            lvAudio.adapter = AudioAdapter(requireContext(), dataListAudio);

            var listGroup = arrayListOf<AudioGroup>()
            listGroup.add(AudioGroup("Popular", dataListAudio))

            lvGroupAudios.layoutManager = layoutManagerVertical;
            lvGroupAudios.adapter = AudioGroupAdapter(requireContext(), listGroup);
//            lvVerticalAudio.isNestedScrollingEnabled = false;

            refreshLayout.apply {
                setOnRefreshListener {
                    isRefreshing = false
                }
            }
            appToolBar.setOnClickLeft{
                val drawer= AppDrawer(requireContext())
                drawer.show()
            }

        }

    }

    private fun initFakeData() {
        dataListAudio.add(
            AudioBook(
                "https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg",
                "",
                "Nhất Niệm Vĩnh Hằng"
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
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }
}