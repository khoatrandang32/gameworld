package com.kflower.gameworld.ui.downloadColection.downloading

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.fetchAudio
import com.kflower.gameworld.adapter.DownloadingAudioAdapter
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.common.getAudioEpFromUri
import com.kflower.gameworld.common.getAudioIdFromUri
import com.kflower.gameworld.databinding.DownloadingTabViewBinding
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.DownloadAudio
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2.Status
import com.tonyodev.fetch2core.DownloadBlock

class DownloadingTabFragment : BaseChildFragment() {

    companion object {
        fun newInstance() = DownloadingTabFragment()
    }

    lateinit var binding: DownloadingTabViewBinding;
    lateinit var viewModel: DownloadingTabViewModel;
    lateinit var adapter: DownloadingAudioAdapter;
    lateinit var fetchListener: FetchListener


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DownloadingTabViewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(DownloadingTabViewModel::class.java)

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

        adapter = DownloadingAudioAdapter(requireContext());
        binding.apply {
            rvDownload.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvDownload.adapter = adapter;
        }

        fetchListener = object :FetchListener{
            override fun onAdded(download: Download) {
                adapter.addDownload(download.toAudioDownload())
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(download: Download) {
                adapter.removeDownload(download.toAudioDownload())
                adapter.notifyDataSetChanged()

            }

            override fun onCompleted(download: Download) {
                adapter.removeDownload(download.toAudioDownload())
                adapter.notifyDataSetChanged()

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
                adapter.updateProgress(download.id.toString(),download.progress)
                adapter.notifyDataSetChanged()
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

        fetchAudio.addListener(fetchListener)

        fetchAudio.getDownloadsWithStatus(Status.DOWNLOADING) { listDownloads ->

            listDownloads.forEach { download ->
                adapter.addDownload(download.toAudioDownload())
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        fetchAudio.removeListener(fetchListener)
    }

    fun Download.toAudioDownload():DownloadAudio{
        return DownloadAudio(
            id = this.id.toString(),
            audioId = this.fileUri.toString()
                .getAudioIdFromUri(MyApplication.mAppContext as Activity),
            state = DownloadState.DOWNLOADING,
            ep = this.fileUri.toString()
                .getAudioEpFromUri(MyApplication.mAppContext as Activity),
            uri = this.fileUri.toString(),
            progress = this.progress
        )
    }

    override fun onBackPressed() {
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}