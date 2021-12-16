package com.kflower.gameworld.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kflower.gameworld.R
import com.kflower.gameworld.common.components.AppBottomBarInterface
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.MainFragmentBinding
import com.kflower.gameworld.ui.main.categories.CategoriesFragment
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.profile.ProfileFragment
import com.kflower.gameworld.ui.main.search.SearchFragment
import kotlin.math.log


open class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    lateinit var binding: MainFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= MainFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutHomeNav, HomeFragment.newInstance())
                .commitNow()
        }
        binding.bottomBar.setOnTabChange(object :AppBottomBarInterface{
            override fun onChangeTab(index: Int) {
                when (index) {
                    0 -> goToTab(HomeFragment.newInstance())
                    1 -> goToTab(HomeFragment())
                    2 -> goToTab(CategoriesFragment.newInstance())
                    3 -> goToTab(ProfileFragment.newInstance())

                }
            }

        })
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }
    protected fun goToTab(newFragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }


}