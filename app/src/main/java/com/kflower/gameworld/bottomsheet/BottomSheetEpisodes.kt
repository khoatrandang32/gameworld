package com.kflower.gameworld.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioEpAdapter
import com.kflower.gameworld.model.AudioBook
import java.lang.RuntimeException


class BottomSheetEpisodes : BottomSheetDialogFragment {
    lateinit var episodes: MutableList<String>;
    lateinit var listener: AudioEpAdapter.AudioEpListener;
    lateinit var audio: AudioBook


    constructor(
        episodes: MutableList<String>,
        audio: AudioBook,
        listener: AudioEpAdapter.AudioEpListener
    ) {
        this.episodes = episodes
        this.listener = listener;
        this.audio = audio;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.bottom_sheet_select_ep, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        var imgClose = view.findViewById<ImageView>(R.id.imgClose)
        imgClose.setOnClickListener {
            dismiss()
        }
        recyclerView.layoutManager = GridLayoutManager(context, 3);

        recyclerView.adapter = AudioEpAdapter(
            requireContext(),
            episodes,
            listener, audio,
        );
        return view;
    }

    override fun onStart() {
        super.onStart()
        val appView = view
        appView?.post {
            val parent = appView.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = appView.measuredHeight
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
            MyApplication.mediaPlayer.currentMediaItemIndex,
            500
        );
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "ActionBottomDialog"

    }
}