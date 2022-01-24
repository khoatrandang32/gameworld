package com.kflower.gameworld.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioEpAdapter
import java.lang.RuntimeException


class BottomSheetEpisodes : BottomSheetDialogFragment {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.bottom_sheet_select_ep, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3);

        recyclerView.adapter = AudioEpAdapter(
            requireContext(),
            episodes,
            listener);
        return view;
    }

    lateinit var episodes: MutableList<String>;
    lateinit var listener: AudioEpAdapter.AudioEpListener;

    constructor(episodes: MutableList<String>,listener:AudioEpAdapter.AudioEpListener ) {
        this.episodes = episodes
        this.listener= listener;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(MyApplication.mediaPlayer.currentMediaItemIndex,500);
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        const val TAG = "ActionBottomDialog"

    }
}