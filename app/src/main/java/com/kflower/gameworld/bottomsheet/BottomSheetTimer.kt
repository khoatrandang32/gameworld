package com.kflower.gameworld.bottomsheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioEpAdapter

class BottomSheetTimer : BottomSheetDialogFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.bottom_sheet_timer, container, false)

        return view;
    }
    constructor() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        dialog?.window?.setDimAmount(0f);
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        const val TAG = "BottomSheetTimer"

    }
}