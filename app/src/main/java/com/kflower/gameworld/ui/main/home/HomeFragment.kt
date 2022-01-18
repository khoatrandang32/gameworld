package com.kflower.gameworld.ui.main.home

import android.os.Bundle
import android.window.SplashScreen
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.adapter.AudioGroupAdapter
import com.kflower.gameworld.dialog.AppDrawer
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.AudioGroup
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.main.MainFragment
import com.kflower.gameworld.ui.splash.SplashFragment


class HomeFragment() : BaseFragment() {


    private lateinit var viewModel: HomeViewModel

    lateinit var binding: HomeFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //
        viewModel.getAudioList();
        //
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerVertical =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.listAudio.observe(this, {
            binding.apply {
                lvAudio.layoutManager = layoutManager;
                lvAudio.adapter = AudioAdapter(requireContext(), viewModel.listAudio.value!!);

                var listGroup = arrayListOf<AudioGroup>()
                listGroup.add(AudioGroup("Popular", viewModel.listAudio.value!!))
                lvGroupAudios.layoutManager = layoutManagerVertical;
                lvGroupAudios.adapter =
                    AudioGroupAdapter(requireContext(), listGroup, object : OnClickAudioBook {
                        override fun onClick(item: AudioBook) {
                            parentNavigateTo(AudioDetailFragment(item))
                        }
                    });
            }
        })
        //


        binding.apply {
            binding.containerLayout.setStatusBarColor(resources.getColor(R.color.main_color))
            //

            viewModel.listAudio.value?.let {

            }
            refreshLayout.apply {
                setOnRefreshListener {
                    viewModel.getAudioList();
                    isRefreshing = false
                }
            }
//            appToolBar.setOnClickLeft {
//                val drawer = AppDrawer(requireContext())
//                drawer.show()
//            }

        }

    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }
}