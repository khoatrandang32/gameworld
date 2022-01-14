package com.kflower.gameworld

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.core.BaseActivity
import com.kflower.gameworld.ui.splash.SplashFragment




class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setDefaultFragment(): Fragment {
        return SplashFragment();
    }

    override fun onDestroy() {
        PlayAudioManager.killMediaPlayer()
        super.onDestroy()
    }


}