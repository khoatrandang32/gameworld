package com.kflower.gameworld.dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.kflower.gameworld.databinding.DialogLoadingBinding

class AppDrawer: Dialog {
    lateinit var binding: DialogLoadingBinding;

    constructor(context: Context):super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoadingBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val window = window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window!!.setBackgroundDrawableResource(R.color.transparent)



    }
}