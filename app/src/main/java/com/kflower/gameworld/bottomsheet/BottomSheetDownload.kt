package com.kflower.gameworld.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.fetchAudio
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioEpAdapter
import com.kflower.gameworld.adapter.SelectedDownloadAdapter
import com.kflower.gameworld.adapter.SmallCategoriesAdapter
import com.kflower.gameworld.common.getDownloadSpecificPath
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.model.AudioBook
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock

class BottomSheetDownload : BottomSheetDialogFragment {
    lateinit var episodes: MutableList<String>;
    lateinit var audio: AudioBook;
    lateinit var selectedDownloadAdapter: SelectedDownloadAdapter;
    lateinit var audioAdapter: AudioEpAdapter;
    lateinit var fetchListener: FetchListener

    constructor(
        episodes: MutableList<String>,
        audio: AudioBook,
    ) {
        this.episodes = episodes
        this.audio = audio;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.bottom_sheet_select_download, container, false)
        selectedDownloadAdapter = SelectedDownloadAdapter(requireContext());

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerViewDownload = view.findViewById<RecyclerView>(R.id.recyclerViewDownload)
        var btnDone = view.findViewById<TextView>(R.id.btnDone)
        var txtTotal = view.findViewById<TextView>(R.id.txtTotal)

        var imgClose = view.findViewById<ImageView>(R.id.imgClose)
        imgClose.setOnClickListener {
            dismiss()
        }

        recyclerView.layoutManager = GridLayoutManager(context, 3);


        //


        audioAdapter = AudioEpAdapter(
            requireContext(),
            episodes,
            object : AudioEpAdapter.AudioEpListener {
                override fun onClick(audioUrl: String, position: Int) {
                    selectedDownloadAdapter.setData(audioAdapter.listDownload)
                    btnDone.isEnabled = !audioAdapter.listDownload.isNullOrEmpty();
                    txtTotal.text = "Bạn đã chọn ${audioAdapter.listDownload.size} tập"
                }

            },
            audio, true
        );
        recyclerView.adapter = audioAdapter;

        var curItem = MyApplication.downloadTable.findDownloadByAudioId(
            audio.id,
            mediaPlayer.currentMediaItemIndex + 1
        )
        if (curItem == null || (curItem.state != DownloadState.DOWNLOADING && curItem.state != DownloadState.COMPLETED)) {
            audioAdapter.listDownload.add(mediaPlayer.currentMediaItemIndex);
            audioAdapter.notifyDataSetChanged()
            selectedDownloadAdapter.setData(audioAdapter.listDownload)
            txtTotal.text = "Bạn đã chọn ${audioAdapter.listDownload.size} tập"
        }


        //
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        recyclerViewDownload.layoutManager = layoutManager;
        recyclerViewDownload.adapter = selectedDownloadAdapter;
        ///

        val offsetFromTop = 400
        (dialog as? BottomSheetDialog)?.behavior?.apply {
//            isFitToContents = false
//            expandedOffset = offsetFromTop
            state = BottomSheetBehavior.STATE_EXPANDED
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED || newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })
        }

        btnDone.setOnClickListener {
            var listRequest = mutableListOf<Request>()
            audioAdapter.listDownload.forEach { itemDownload ->
                val dirPath = getDownloadSpecificPath(
                    requireActivity(),
                    audio.id,
                    itemDownload + 1
                )

                val request = Request(episodes[itemDownload], dirPath)
                request.priority = Priority.HIGH
                request.networkType = NetworkType.ALL
                listRequest.add(request)
            }

            fetchAudio.enqueue(listRequest) {
                it.forEach { pair: Pair<Request, Error> ->
                    Log.d(TAG, "onCreateView: " + pair.second.httpResponse?.errorResponse)
                }
            }
            dismiss()

        }
        fetchListener= object :FetchListener{
            override fun onAdded(download: Download) {
            }

            override fun onCancelled(download: Download) {
            }

            override fun onCompleted(download: Download) {
                audioAdapter.updateDownload(download)

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
                audioAdapter.updateDownload(download)
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

        return view;
    }

    override fun onDestroy() {
        fetchAudio.removeListener(fetchListener)
        super.onDestroy()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
            mediaPlayer.currentMediaItemIndex,
            500
        );
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "BottomSheetDownload"

    }
}