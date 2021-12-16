package com.kflower.gameworld.ui.main.search

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.databinding.SearchFragmentBinding
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel

class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    lateinit var binding: SearchFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= SearchFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}