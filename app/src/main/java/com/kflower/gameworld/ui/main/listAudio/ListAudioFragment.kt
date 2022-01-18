package com.kflower.gameworld.ui.main.listAudio

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.adapter.AudioVerticalAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.ListAudioFragmentBinding
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.Category
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.main.profile.ProfileFragment
import com.kflower.gameworld.ui.main.profile.ProfileViewModel

class ListAudioFragment(private val itemCategory: Category) : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ListAudioViewModel
    private lateinit var audioAdapter: AudioVerticalAdapter;

    lateinit var binding: ListAudioFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListAudioFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ListAudioViewModel::class.java)
        binding.viewModel= viewModel;
        binding.lifecycleOwner= this
        itemCategory.id?.let {
            viewModel.getAudioListByCategory(it)
        }

        viewModel.curCategory.postValue(itemCategory);

        viewModel.listAudio.observe(this,{
            audioAdapter.setData(it);
        })

        audioAdapter= AudioVerticalAdapter(requireContext(), viewModel.listAudio.value!!, object : OnClickAudioBook {
            override fun onClick(item: AudioBook) {
                navigateTo(AudioDetailFragment(item))
            }
        });
        binding?.apply {
            rvAudio.layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvAudio.adapter=audioAdapter;
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}