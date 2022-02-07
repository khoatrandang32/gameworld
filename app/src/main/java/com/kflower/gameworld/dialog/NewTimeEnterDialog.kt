package com.kflower.gameworld.dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.kflower.gameworld.adapter.TimeListAdapter
import com.kflower.gameworld.common.components.AppEditText
import com.kflower.gameworld.databinding.DialogLoadingBinding
import com.kflower.gameworld.databinding.DialogNewTimeEnterBinding

class NewTimeEnterDialog : Dialog {
    lateinit var binding: DialogNewTimeEnterBinding;
    lateinit var listener: TimeListAdapter.TimeClickListener;

    constructor(context: Context, listener: TimeListAdapter.TimeClickListener):super(context){
        this.listener= listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogNewTimeEnterBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val window = window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        window!!.setBackgroundDrawableResource(R.color.transparent)
        binding.btnConfirm.setOnClickListener {
            listener.onClick(Integer.valueOf(binding.edtTime.getText()))
        }
        binding?.apply {
            edtTime.setOnChangeText(
                object :AppEditText.OnChangeText{
                    override fun onChangeText(text: String) {
                        btnConfirm.isEnabled= text.isNotEmpty()
                    }

                }
            )
        }

    }
}