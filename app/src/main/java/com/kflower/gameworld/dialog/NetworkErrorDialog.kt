package com.kflower.gameworld.dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.kflower.gameworld.databinding.DialogLoadingBinding
import com.kflower.gameworld.databinding.DialogNetworkErrorBinding

class NetworkErrorDialog : Dialog {
    lateinit var binding: DialogNetworkErrorBinding;
    constructor(context: Context):super(context){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogNetworkErrorBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val window = window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        window!!.setBackgroundDrawableResource(R.color.transparent)

    }
}