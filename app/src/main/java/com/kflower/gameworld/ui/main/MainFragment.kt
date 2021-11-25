package com.kflower.gameworld.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.kflower.gameworld.R
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.MainFragmentBinding
import com.kflower.gameworld.databinding.SplashFragmentBinding


class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


    }

    override fun getLayoutBinding(): ViewDataBinding {
        return MainFragmentBinding.inflate(layoutInflater)
    }

}