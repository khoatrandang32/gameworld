package com.kflower.gameworld.bottomsheet

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioEpAdapter
import com.kflower.gameworld.adapter.TimeListAdapter
import android.content.Intent
import com.kflower.gameworld.services.CountDownServices


class BottomSheetTimer : BottomSheetDialogFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var adapter: TimeListAdapter;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.bottom_sheet_timer, container, false)

        var rvTimeItem = view.findViewById<RecyclerView>(R.id.rvTimeItem);
        var btnDone = view.findViewById<TextView>(R.id.btnDone)


        rvTimeItem.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //
        (dialog as BottomSheetDialog)?.apply {
            behavior.isDraggable = false
            behavior.isHideable = false
        }
        isCancelable = false
        var list = mutableListOf<Int>();
        list.add(15);
        list.add(30);
        list.add(60);
        adapter =
            TimeListAdapter(requireContext(), list, object : TimeListAdapter.TimeClickListener {
                override fun onClick(item: Int) {
                    btnDone.isEnabled= true
                }

            })
        rvTimeItem.adapter = adapter

        btnDone.setOnClickListener {
            dismiss()
            var timeset = list[adapter.selectedIndex] * 60000L
            context?.let {
                MyApplication.startTimer(timeset, it);
            }
        }
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