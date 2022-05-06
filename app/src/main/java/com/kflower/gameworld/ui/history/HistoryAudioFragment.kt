package com.kflower.gameworld.ui.history

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.adapter.AudioVerticalAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HistoryAudioFragmentBinding
import com.kflower.gameworld.databinding.TimerFragmentBinding
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.timer.TimerViewModel

class HistoryAudioFragment : BaseFragment() {

    lateinit var binding: HistoryAudioFragmentBinding;
    lateinit var viewModel: HistoryAudioViewModel;
    private lateinit var audioAdapter: AudioVerticalAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HistoryAudioFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HistoryAudioViewModel::class.java)

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

        viewModel.setUpData()

        audioAdapter= AudioVerticalAdapter(requireContext(), viewModel.listAudio.value!!, object :
            OnClickAudioBook {
            override fun onClick(item: AudioBook) {
                navigateTo(AudioDetailFragment(item))
            }
        });

        binding?.apply {
            rvAudio.layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvAudio.adapter=audioAdapter;
        }
        viewModel.listAudio.observe(this,{
            audioAdapter.setData(viewModel.listAudio.value!!)
        })

    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}