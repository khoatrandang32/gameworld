package com.kflower.gameworld.ui.splash

import android.R.attr
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.kflower.gameworld.R
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.ui.main.MainFragment
import android.R.attr.button

import android.view.Gravity

import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.kflower.gameworld.databinding.SplashFragmentBinding
import android.R.attr.button
import androidx.transition.ArcMotion
import androidx.transition.ChangeBounds
import android.R.attr.button
import android.R.attr.button
import android.content.res.Resources
import android.view.View
import android.util.DisplayMetrics
import android.text.Editable

import android.text.TextWatcher
import androidx.constraintlayout.widget.ConstraintLayout


class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var binding: SplashFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashFragmentBinding.inflate(layoutInflater)


        Handler().postDelayed({
            val changeBounds = ChangeBounds();
            changeBounds.duration = 500;
            changeBounds.setPathMotion(ArcMotion())
            TransitionManager.beginDelayedTransition(
                binding.layoutContainer,
                changeBounds
            )
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                height= Resources.getSystem().displayMetrics.heightPixels/3
            }
            binding.logoLayout.layoutParams = params
//            binding.loginLayout.layoutParams = params;
        }, 2000)



    }


    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }

}