package com.kflower.gameworld.ui.main.profile

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.databinding.ProfileFragmentBinding
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    lateinit var binding: ProfileFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ProfileFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}