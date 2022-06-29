package com.kflower.gameworld.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kflower.gameworld.MyApplication.Companion.appFragmentManager
import com.kflower.gameworld.R
import com.kflower.gameworld.bottomsheet.BottomSheetMedia
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.components.AppBottomBarInterface
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.constants.StatusMode
import com.kflower.gameworld.databinding.MainFragmentBinding
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.main.categories.CategoriesFragment
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.profile.ProfileFragment
import com.kflower.gameworld.ui.main.search.SearchFragment
import com.kflower.gameworld.ui.play.PlayAudioFragment
import retrofit2.Callback


open class MainFragment : BaseChildFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    lateinit var binding: MainFragmentBinding;

    var currentTab = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val homeFragment = HomeFragment();
        val searchFragment = SearchFragment();
        val categoriesFragment = CategoriesFragment();
        val profileFragment = ProfileFragment();

        if (savedInstanceState == null) {
            goToTab(homeFragment,false)
        }
        binding.bottomBar.setOnTabChange(object : AppBottomBarInterface {
            override fun onChangeTab(index: Int) {
                var isRightSlide = currentTab < index;
                when (index) {
                    0 -> goToTab(homeFragment, isRightSlide)
                    1 -> goToTab(searchFragment, isRightSlide)
                    2 -> goToTab(categoriesFragment, isRightSlide)
                    3 -> goToTab(profileFragment, isRightSlide)
                }
                currentTab = index
            }

        })
        binding.miniMedia.setOnClick {
            PlayAudioManager.playingAudio?.let {
//                navigateTo(PlayAudioFragment(it))
                showBottomSheet(it)

            }
        }
        setStatusBarMode(StatusMode.DarkMode)

    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

    protected fun goToTab(newFragment: Fragment, isRightSlide: Boolean) {

        val ft: FragmentTransaction = appFragmentManager!!.beginTransaction()
        if (isRightSlide) {

            ft.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
            )
        } else {
            ft.setCustomAnimations(
                R.anim.slide_left,
                R.anim.slide_right,
            )
        }
        ft.replace(R.id.frameLayoutHomeNav, newFragment)

        ft.addToBackStack(null)
        ft.commit()


//        childFragmentManager.beginTransaction()
//            .replace(R.id.frameLayoutHomeNav, newFragment)
//            .commitNow()
    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        super.onResume()
        binding.miniMedia.refresh()
    }
}