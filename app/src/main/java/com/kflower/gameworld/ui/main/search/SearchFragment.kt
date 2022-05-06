package com.kflower.gameworld.ui.main.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.adapter.AudioVerticalAdapter
import com.kflower.gameworld.adapter.GroupSearchAdapter
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.databinding.SearchFragmentBinding
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.SearchGroup
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel

class SearchFragment : BaseChildFragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var audioAdapter: AudioVerticalAdapter;
    private lateinit var listAudios: MutableList<AudioBook>;

    lateinit var binding: SearchFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchFragmentBinding.inflate(layoutInflater)
        listAudios = mutableListOf();
        viewModel = SearchViewModel();
        binding.lifecycleOwner = this;

        binding.viewModel = viewModel;

        viewModel.searchText.observe(this,{
            if(it.isNullOrEmpty()){
                viewModel.isSearching.postValue(false)
                viewModel.listAudio.postValue(mutableListOf())

            }
        })

        binding.edtSearch.setOnPressDone { v, actionId, event ->
            if (!viewModel.searchText.value.isNullOrEmpty()) {
                viewModel.getAudioList(viewModel.searchText.value!!)
            }
            false
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        viewModel.listAudio.observe(this, {
//            listAudios= it;
//            audioAdapter.notifyDataSetChanged()
            audioAdapter.setData(it);
        });

        binding.shimmerLayout.startShimmer();
        val layoutManagerVertical =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //
        var listSearch = mutableListOf<String>();
        listSearch.add("Nhat niem vinh hang")
        listSearch.add("Pham nhan tu tien")
        listSearch.add("Kim dung")
        listSearch.add("Kiem hiep")
        listSearch.add("Bo gia")
        listSearch.add("Su im lang cua bay cuu")
        listSearch.add("Mich tien lo")

        var listSearchGr = mutableListOf<SearchGroup>();
        listSearchGr.add(SearchGroup("Currently", listSearch))


        audioAdapter =
            AudioVerticalAdapter(requireContext(), listAudios, object : OnClickAudioBook {
                override fun onClick(item: AudioBook) {
                    parentNavigateTo(AudioDetailFragment(item))
                }
            });
        binding.apply {
            lvGroupSearch.layoutManager = layoutManagerVertical
            lvGroupSearch.adapter = GroupSearchAdapter(requireContext(), listSearchGr)
            lvAudioResult.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            lvAudioResult.adapter = audioAdapter;
            checkConnectionLayout.setOnRetry {
                viewModel?.searchText?.value?.apply {
                    viewModel?.getAudioList(this)
                }
            }
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isError.value == true) {
            viewModel.searchText.value?.apply {
                viewModel?.getAudioList(this)
            }
        }
    }

}