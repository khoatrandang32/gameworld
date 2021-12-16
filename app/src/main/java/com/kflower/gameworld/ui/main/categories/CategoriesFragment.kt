package com.kflower.gameworld.ui.main.categories

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.CategoriesFragmentBinding
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel

class CategoriesFragment  : BaseFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel

    lateinit var binding: CategoriesFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= CategoriesFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}