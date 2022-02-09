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
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.mAppContext
import com.kflower.gameworld.ui.timer.TimerFragment
import com.tonyodev.fetch2core.Func
import java.util.*
import kotlin.math.log


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val currentTime: Date = Calendar.getInstance().getTime()

        super.onCreate(savedInstanceState)
        mAppContext= this;
        MyApplication.fetchAudio.getDownloads(Func {
            if (it.isNotEmpty()) {
                Log.d(
                    "KHOA", "onCreate: " + it[0].fileUri+ " - "+it[0].url)
            }
        })
    }

    override fun setDefaultFragment(): Fragment {
        return SplashFragment();
//        return TimerFragment();
    }

    override fun onDestroy() {
        PlayAudioManager.killMediaPlayer()
        super.onDestroy()
    }


}