package com.kflower.gameworld.ui.downloadColection.downloaded

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.adapter.AudioVerticalAdapter
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.databinding.DownloadedTabViewBinding
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.main.MainFragment
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock

class DownloadedTabFragment() : BaseChildFragment() {

    lateinit var binding: DownloadedTabViewBinding;
    lateinit var viewModel: DownloadedTabViewModel;
    lateinit var adapter: AudioVerticalAdapter;
    lateinit var listDownloadedAudio: MutableList<AudioBook>
    lateinit var fetchListener: FetchListener

    var listAudioId = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DownloadedTabViewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(DownloadedTabViewModel::class.java)
        listDownloadedAudio= mutableListOf()

        binding.apply {
            adapter = AudioVerticalAdapter(requireContext(), listDownloadedAudio,
                object : OnClickAudioBook {
                    override fun onClick(item: AudioBook) {
                        parentNavigateTo(AudioDetailFragment(item, true))
                    }

                });
            rvDownload.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvDownload.adapter = adapter;
        }

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

    }

    private fun setUpDataDownload() {
        var haveNewData= false;
        var listDownloaded =
            MyApplication.downloadTable.findDownloadsByState(DownloadState.COMPLETED);

        listDownloaded?.forEach { item ->
            if (!listAudioId.contains(item.audioId)) {
                listAudioId.add(item.audioId)
                haveNewData= true
            }
        }
        if(haveNewData){
            listDownloadedAudio= mutableListOf()
            listAudioId.forEach {
                var audioBookList = MyApplication.audioTable.findAudio(it);

                if (audioBookList.size > 0) {
                    var audioBook = audioBookList[0];
                    listDownloadedAudio.add(audioBook)
                }
            }

        }

        adapter.setData(listDownloadedAudio)
        //

        fetchListener = object :FetchListener{
            override fun onAdded(download: Download) {

            }

            override fun onCancelled(download: Download) {

            }

            override fun onCompleted(download: Download) {
                setUpDataDownload()
            }

            override fun onDeleted(download: Download) {
            }

            override fun onDownloadBlockUpdated(
                download: Download,
                downloadBlock: DownloadBlock,
                totalBlocks: Int
            ) {

            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
            }

            override fun onPaused(download: Download) {
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            }

            override fun onRemoved(download: Download) {
            }

            override fun onResumed(download: Download) {
            }

            override fun onStarted(
                download: Download,
                downloadBlocks: List<DownloadBlock>,
                totalBlocks: Int
            ) {
            }

            override fun onWaitingNetwork(download: Download) {
            }

        }

        MyApplication.fetchAudio.addListener(fetchListener)

    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            setUpDataDownload()

        }, 500)
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}