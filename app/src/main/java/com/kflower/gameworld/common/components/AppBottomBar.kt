package com.kflower.gameworld.common.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kflower.gameworld.R
import com.kflower.gameworld.ui.main.categories.CategoriesFragment
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.profile.ProfileFragment
import com.kflower.gameworld.ui.main.search.SearchFragment
import kotlin.math.log

class AppBottomBar:LinearLayout {

    lateinit var btnTabHome :LinearLayout;
    lateinit var btnTabSearch :LinearLayout;
    lateinit var btnTabCategories :LinearLayout;
    lateinit var btnTabProfile :LinearLayout;
    lateinit var imgHome :ImageView;
    lateinit var imgSearch :ImageView;
    lateinit var imgCategories :ImageView;
    lateinit var imgProfile :ImageView;
    //
    lateinit var txtHome :TextView;
    lateinit var txtSearch :TextView;
    lateinit var txtCategories :TextView;
    lateinit var txtProfile :TextView;

    var curTabIndex=0;

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
        btnTabProfile= view.findViewById(R.id.btnTabProfile)
        imgHome= view.findViewById(R.id.imgHome);
        imgSearch= view.findViewById(R.id.imgSearch);
        imgCategories= view.findViewById(R.id.imgCategories);
        imgProfile= view.findViewById(R.id.imgProfile);
        //
        txtHome= view.findViewById(R.id.txtHome);
        txtSearch= view.findViewById(R.id.txtSearch);
        txtCategories= view.findViewById(R.id.txtCategories);
        txtProfile= view.findViewById(R.id.txtProfile);


        if(listener!=null){
            btnTabHome.setOnClickListener {
                onChangeTab(0)
            }
            btnTabSearch.setOnClickListener {
                onChangeTab(1)
            }
            btnTabCategories.setOnClickListener {
                onChangeTab(2)
            }
            btnTabProfile.setOnClickListener {
                onChangeTab(3);
            }
        }
        changeSelectedTabIcon(curTabIndex)
    }
    private fun onChangeTab(index:Int){
        if(curTabIndex!=index) {
            listener.onChangeTab(index)
            curTabIndex = index;
            resetBtn();
            changeSelectedTabIcon(index);
        }
    }

    private fun changeSelectedTabIcon(index:Int) {
        when(index){
            0 ->{
                var icon= resources.getDrawable(R.drawable.ic_home_solid);
                icon.setTint(resources.getColor(R.color.main_color))
                imgHome.setImageDrawable(icon)
                txtHome.setTextColor(resources.getColor(R.color.main_color));

            }
            1 ->{
                var icon= resources.getDrawable(R.drawable.ic_search_solid);
                icon.setTint(resources.getColor(R.color.main_color))
                imgSearch.setImageDrawable(icon)
                txtSearch.setTextColor(resources.getColor(R.color.main_color));
            }
            2 ->{
                var icon= resources.getDrawable(R.drawable.ic_categories_solid);
                icon.setTint(resources.getColor(R.color.main_color))
                imgCategories.setImageDrawable(icon)
                txtCategories.setTextColor(resources.getColor(R.color.main_color));
            }
            3 ->{
                var icon= resources.getDrawable(R.drawable.ic_user_solid);
                icon.setTint(resources.getColor(R.color.main_color))
                imgProfile.setImageDrawable(icon)
                txtProfile.setTextColor(resources.getColor(R.color.main_color));
            }
        }

    }

    private fun resetBtn() {
        imgHome.setImageResource(R.drawable.ic_home_outline);
        imgSearch.setImageResource(R.drawable.ic_search);
        imgCategories.setImageResource(R.drawable.ic_categories_outline);
        imgProfile.setImageResource(R.drawable.ic_user_outline);
        //
        txtHome.setTextColor(context.resources.getColor(R.color.gray_color));
        txtSearch.setTextColor(context.resources.getColor(R.color.gray_color));
        txtCategories.setTextColor(context.resources.getColor(R.color.gray_color));
        txtProfile.setTextColor(context.resources.getColor(R.color.gray_color));

    }

    public fun setOnTabChange(listener:AppBottomBarInterface){
        this.listener= listener;
    }
}