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
import com.kflower.gameworld.adapter.CommentAdapter
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.constants.StatusMode
import com.kflower.gameworld.databinding.AudioDetailFragmentBinding
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.play.PlayAudioFragment
import glimpse.glide.GlimpseTransformation

class AudioDetailFragment(val item: AudioBook) : BaseFragment() {

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
        viewModel.getAudioDetail(item.id)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.viewModel = viewModel;
        binding.imgPlay.setOnClickListener {
            viewModel.item?.let { it ->
                it.value?.let { it2 ->
//                    setStatusBarMode(StatusMode.DarkMode)
                    navigateTo(PlayAudioFragment(it2))
                }
            }
        }

        val layoutManagerVertical =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.item.observe(this, {
            binding.apply {
                Glide.with(requireContext())
                    .load(it.thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(GlimpseTransformation())
                    .into(imgThumbnail)
//                Glide.with(requireContext()).load(it.thumbnailUrl).into(imgThumbnail);
                lvComments.layoutManager = layoutManagerVertical;
                lvComments.adapter = CommentAdapter(requireContext(), it.comments);
                refreshLayout.apply {
                    setOnRefreshListener {
                        viewModel?.getAudioDetail(item.id)
                        isRefreshing = false
                    }
                }
            }
        })

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