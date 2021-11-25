package com.kflower.gameworld

import androidx.fragment.app.Fragment
import com.kflower.gameworld.common.core.BaseActivity
import com.kflower.gameworld.ui.splash.SplashFragment

class MainActivity : BaseActivity() {

    override fun setDefaultFragment(): Fragment {
        return SplashFragment();
    }
}