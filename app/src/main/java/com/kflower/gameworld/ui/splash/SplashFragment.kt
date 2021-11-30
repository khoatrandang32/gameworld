package com.kflower.gameworld.ui.splash

import android.R.attr
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
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
import android.animation.ArgbEvaluator
import android.content.res.Resources
import android.view.View
import android.util.DisplayMetrics
import android.text.Editable

import android.text.TextWatcher
import androidx.constraintlayout.widget.ConstraintLayout
import android.animation.ValueAnimator

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.Activity
import android.graphics.ColorFilter
import com.kflower.gameworld.R
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable
import android.util.Log

import androidx.appcompat.content.res.AppCompatResources
import com.kflower.gameworld.User
import com.kflower.gameworld.database.AppDatabaseHelper
import com.kflower.gameworld.database.UserTable
import kotlin.math.log


class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var binding: SplashFragmentBinding;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = SplashFragmentBinding.inflate(layoutInflater)

        var database = AppDatabaseHelper(context);

        Log.d(
            "KHOA", "onCreate: " + UserTable(null).findUser(context, "Khoadz").get(0).username
        )

        binding.loginBtn.setOnClickListener {
            UserTable(null).addNewUser(context, User("Khoadz", "123456"))
        }

        Handler().postDelayed({
            val changeBounds = ChangeBounds();
            changeBounds.duration = 500;
            changeBounds.setPathMotion(ArcMotion())
            TransitionManager.beginDelayedTransition(
                binding.layoutContainer,
                changeBounds
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                height = Resources.getSystem().displayMetrics.heightPixels / 3
            }
            binding.logoLayout.layoutParams = params
            val mainColor = resources.getColor(R.color.main_color)
            val bgColor = resources.getColor(R.color.background_color)
            updateColor(mainColor, bgColor) { animation ->
                binding.layoutContainer.setBackgroundColor(animation.animatedValue as Int)
            };
            updateColor(bgColor, mainColor) { animation ->
                binding.apply {
                    imgLogo.setColorFilter(animation.animatedValue as Int)
                    txtName.setTextColor(animation.animatedValue as Int)
                    txtSlogan.setTextColor(animation.animatedValue as Int)
                    edtUsername.setText("");

                }

            };
        }, 2000)


    }

    private fun updateColor(colorFrom: Int, colorTo: Int, listener: AnimatorUpdateListener) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 600 // milliseconds
//        colorAnimation.addUpdateListener { animator -> binding.layoutContainer.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.addUpdateListener(listener);
        colorAnimation.start()
    }


    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }

}