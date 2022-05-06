package com.kflower.gameworld.ui.downloadColection

import android.os.Bundle
import android.os.Handler
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
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.DownloadCollectionFragmentBinding
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.interfaces.BottomSheetListener
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.ui.downloadColection.downloaded.DownloadedTabFragment
import com.kflower.gameworld.ui.downloadColection.downloading.DownloadingTabFragment
import android.os.Looper
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.adapter.SectionsPagerAdapter
import com.kflower.gameworld.model.PageView


class DownloadCollectionFragment : BaseFragment() {

    companion object {
        fun newInstance() = DownloadCollectionFragment()
    }

    lateinit var binding: DownloadCollectionFragmentBinding;
    lateinit var viewModel: DownloadCollectionViewModel;
    lateinit var sectionsPagerAdapter: SectionsPagerAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DownloadCollectionFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(DownloadCollectionViewModel::class.java)

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

        sectionsPagerAdapter =
            SectionsPagerAdapter(MyApplication.appFragmentManager!!,lifecycle );
        sectionsPagerAdapter.addFragment(PageView("Đã tải", DownloadedTabFragment()))
        sectionsPagerAdapter.addFragment(PageView("Đang tải", DownloadingTabFragment()))
        sectionsPagerAdapter.notifyDataSetChanged()

        binding?.apply {
           viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = sectionsPagerAdapter.getPageTitle(position)
                viewPager.setCurrentItem(tab.position, true)
            }.attach()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }

}