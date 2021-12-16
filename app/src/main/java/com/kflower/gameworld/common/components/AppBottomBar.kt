package com.kflower.gameworld.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.kflower.gameworld.R
import com.kflower.gameworld.ui.main.categories.CategoriesFragment
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.profile.ProfileFragment
import com.kflower.gameworld.ui.main.search.SearchFragment

class AppBottomBar:LinearLayout {

    lateinit var btnTabHome :LinearLayout;
    lateinit var btnTabSearch :LinearLayout;
    lateinit var btnTabCategories :LinearLayout;
    lateinit var btnTabProfile :LinearLayout;

    lateinit var listener :AppBottomBarInterface;
    constructor(context: Context) : super(context) {
        initView(null)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }
    private fun initView(attrs: AttributeSet?) {
        listener= object :AppBottomBarInterface{
            override fun onChangeTab(index: Int) {

            }
        }
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.app_bottom_bar_layout, null
        )
        view.layoutParams = layoutParams
        this.addView(view)
        btnTabHome= view.findViewById(R.id.btnTabHome);
        btnTabSearch= view.findViewById(R.id.btnTabSearch);
        btnTabCategories= view.findViewById(R.id.btnTabCategories);
        btnTabProfile= view.findViewById(R.id.btnTabProfile);
        if(listener!=null){
            btnTabHome.setOnClickListener {
                listener.onChangeTab(0)
            }
            btnTabSearch.setOnClickListener {
                listener.onChangeTab(1)
            }
            btnTabCategories.setOnClickListener {
                listener.onChangeTab(2)
            }
            btnTabProfile.setOnClickListener {
                listener.onChangeTab(3)
            }
        }
    }
    public fun setOnTabChange(listener:AppBottomBarInterface){
        this.listener= listener;
    }
}