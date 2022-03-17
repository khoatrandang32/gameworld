package com.kflower.gameworld.ui.audioDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.adapter.CommentAdapter
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.common.toBitMap
import com.kflower.gameworld.constants.StatusMode
import com.kflower.gameworld.databinding.AudioDetailFragmentBinding
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.play.PlayAudioFragment
import glimpse.glide.GlimpseTransformation

class AudioDetailFragment(
    val item: AudioBook,
    var isFromDownload: Boolean? = false
) : BaseFragment() {

    private lateinit var viewModel: AudioDetailViewModel

    lateinit var binding: AudioDetailFragmentBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStatusBarMode(StatusMode.LightMode)
        binding = AudioDetailFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this;
        viewModel = ViewModelProvider(this).get(AudioDetailViewModel::class.java)

        viewModel.item.postValue(item)
//        viewModel.getAudioDetail(item.id)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.viewModel = viewModel;
        binding.imgPlay.setOnClickListener {
            navigateTo(PlayAudioFragment(item))
        }

        val layoutManagerVertical =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.apply {
            imgThumbnail.setImageBitmap(item.imgBase64.toBitMap())
            lvComments.layoutManager = layoutManagerVertical;
            lvComments.adapter = CommentAdapter(requireContext(), mutableListOf());
            refreshLayout.apply {
                setOnRefreshListener {
                    viewModel?.getAudioDetail(item.id)
                    isRefreshing = false
                }
            }
        }
        MyApplication.audioTable.addNewAudio(item)

    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

    override fun onResume() {
//        setStatusBarMode(StatusMode.LightMode)
        super.onResume()
    }

    override fun onBackPressed() {
//        setStatusBarMode(StatusMode.DarkMode)
        super.onBackPressed()
    }

}