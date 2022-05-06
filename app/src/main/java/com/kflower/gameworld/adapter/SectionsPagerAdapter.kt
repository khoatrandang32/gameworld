package com.kflower.gameworld.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kflower.gameworld.model.PageView

class SectionsPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {
    private val fragmentList: MutableList<PageView> = ArrayList()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].page
    }

    fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].title
    }
    fun addFragment(fragment: PageView) {
        fragmentList.add(fragment)
    }
}