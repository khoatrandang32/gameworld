package com.kflower.gameworld.ui.downloadColection.downloaded

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.audioTable
import com.kflower.gameworld.MyApplication.Companion.downloadTable
import com.kflower.gameworld.MyApplication.Companion.listDownloaded
import com.kflower.gameworld.adapter.AudioVerticalAdapter
import com.kflower.gameworld.adapter.DownloadingAudioAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.DownloadedTabViewBinding
import com.kflower.gameworld.databinding.DownloadingTabViewBinding
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.downloadColection.downloading.DownloadingTabFragment
import com.kflower.gameworld.ui.downloadColection.downloading.DownloadingTabViewModel

class DownloadedTabFragment : BaseFragment() {

    companion object {
        fun newInstance() = DownloadedTabFragment()
    }

    lateinit var binding: DownloadedTabViewBinding;
    lateinit var viewModel: DownloadedTabViewModel;
    lateinit var adapter: AudioVerticalAdapter;
    lateinit var listDownloadedAudio: MutableList<AudioBook>;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DownloadedTabViewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(DownloadedTabViewModel::class.java)
        listDownloadedAudio = mutableListOf()
//        binding.apply {
//
//            setUpDataDownload();
//
//            adapter = AudioVerticalAdapter(requireContext(), listDownloadedAudio,
//                object : OnClickAudioBook {
//                    override fun onClick(item: AudioBook) {
//                        parentNavigateTo(AudioDetailFragment(item, true))
//                    }
//
//                });
//            rvDownload.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            rvDownload.adapter = adapter;
//        }
//        listDownloaded.observe(this, { _ ->
//            setUpDataDownload()
//        })
        binding.viewModel = viewModel;
        binding.lifecycleOwner = this



    }

    private fun setUpDataDownload() {
        var listDownloaded = downloadTable.findDownloadsByState(DownloadState.COMPLETED);

        listDownloaded?.forEach { item ->
            var audioBookList = audioTable.findAudio(item.audioId);
            if (audioBookList.size > 0) {
                var audioBook = audioBookList[0];
                if (!listDownloadedAudio.contains(audioBook)) {
                    listDownloadedAudio.add(audioBook)
                }
            }

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}