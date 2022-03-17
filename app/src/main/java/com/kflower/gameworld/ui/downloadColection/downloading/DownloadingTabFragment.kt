package com.kflower.gameworld.ui.downloadColection.downloading

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.listDownloading
import com.kflower.gameworld.adapter.DownloadingAudioAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.DownloadingTabViewBinding

class DownloadingTabFragment : BaseFragment() {

    companion object {
        fun newInstance() = DownloadingTabFragment()
    }

    lateinit var binding: DownloadingTabViewBinding;
    lateinit var viewModel: DownloadingTabViewModel;
    lateinit var adapter: DownloadingAudioAdapter;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DownloadingTabViewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(DownloadingTabViewModel::class.java)

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listData= if(MyApplication.listDownloading.value==null) mutableListOf() else MyApplication.listDownloading.value
        adapter = DownloadingAudioAdapter(requireContext(), listData!!);
        binding.apply {
            rvDownload.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvDownload.adapter = adapter;
        }
        listDownloading.observe(this,{
            adapter.setListData(it)
        })

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}