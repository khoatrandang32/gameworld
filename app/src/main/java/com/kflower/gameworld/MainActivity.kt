package com.kflower.gameworld

import android.app.Notification
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.core.BaseActivity
import com.kflower.gameworld.ui.splash.SplashFragment
import android.app.NotificationManager
import android.content.Context
import com.tonyodev.fetch2core.Func
import kotlin.math.log


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fetchAudio.getDownloads(Func {
            Log.d("KHOA", "onCreate: "+it[0].progress)
        })

    }

    override fun setDefaultFragment(): Fragment {
        return SplashFragment();
    }

    override fun onDestroy() {
        PlayAudioManager.killMediaPlayer()
        super.onDestroy()
    }


}