package com.kflower.gameworld.ui.downloadColection

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.DownloadPagerAdapter
import com.kflower.gameworld.bottomsheet.BottomSheetTimer
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.DownloadCollectionFragmentBinding
import com.kflower.gameworld.interfaces.BottomSheetListener
import com.kflower.gameworld.ui.downloadColection.downloaded.DownloadedTabFragment
import com.kflower.gameworld.ui.downloadColection.downloading.DownloadingTabFragment

class DownloadCollectionFragment : BaseFragment() {

    companion object {
        fun newInstance() = DownloadCollectionFragment()
    }

    lateinit var binding: DownloadCollectionFragmentBinding;
    lateinit var viewModel: DownloadCollectionViewModel;
    lateinit var downloadingTab:DownloadingTabFragment
    lateinit var downloadedTab:DownloadedTabFragment


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //
        downloadingTab= DownloadingTabFragment()
        downloadedTab= DownloadedTabFragment()

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.downloadTabViewContainer, downloadedTab)
                .commitNow()
        }

        binding = DownloadCollectionFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(DownloadCollectionViewModel::class.java)

        binding.apply {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position){
                        0->changeTab(downloadedTab)
                        1->changeTab(downloadingTab)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })
        }

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }

    fun changeTab(newFragment: Fragment) {

        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
        )
        ft.replace(R.id.downloadTabViewContainer, newFragment)

        ft.addToBackStack(null)
        ft.commit()

    }
}