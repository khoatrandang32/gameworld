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
import android.content.Intent
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.mAppContext
import com.kflower.gameworld.ui.timer.TimerFragment
import com.tonyodev.fetch2core.Func
import java.util.*
import kotlin.math.log
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.common.Key
import com.kflower.gameworld.services.MediaSessionService
import android.view.ViewGroup

import android.view.MotionEvent

import android.view.View.OnTouchListener

import android.widget.EditText
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.kflower.gameworld.MyApplication.Companion.audioTable
import com.kflower.gameworld.MyApplication.Companion.downloadTable
import com.kflower.gameworld.common.getAudioIdFromUri
import com.kflower.gameworld.enum.DownloadState


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var idResult= getPreferences(MODE_PRIVATE).getString(Key.KEY_APP_ID, null);

        if(idResult.isNullOrEmpty()){
            var  UUID = UUID.randomUUID()
            var id= UUID.toString().split("-")[0]
            val editor = getPreferences(MODE_PRIVATE).edit()
            editor.putString(Key.KEY_APP_ID, id);
            editor.commit();
        }


        super.onCreate(savedInstanceState)
        mAppContext= this;
        MyApplication.fetchAudio.getDownloads(Func {
            if (it.isNotEmpty()) {
                Log.d(
                    "KHOA", "getDownloads: " + it[0].fileUri+ " - "+it[0].url)
            }
        })

        val sharedPref = getSharedPreferences(Key.KEY_STORE, MODE_PRIVATE)
        val playingAudioId = sharedPref.getString(Key.KEY_PLAYING_AUDIO_ID, "")
        var result= audioTable.findAudio(playingAudioId)

        if(result.size>0){
            var item= result[0];
            var episodes= mutableListOf<String>()



            for(i in 1..item.episodesAmount){
                episodes.add(String.format(item.baseEpisode,i))
            }
            PlayAudioManager.preparePlayNewAudioList(
                episodes
            )

            PlayAudioManager.playingAudio = item

            ContextCompat.startForegroundService(
                this,
                Intent(this, MediaSessionService::class.java)
            )
            PlayAudioManager.isShowNoti = true;

            mediaPlayer.seekTo(item.curEp,item.progress)


        }
    }

    override fun onResume() {
        super.onResume()
        var listDownload= downloadTable.findDownloadsByState(DownloadState.COMPLETED)
        if(listDownload.size>0){
            listDownload.forEach {
                downloadTable.findAndCheckDownloadByAudioId(it.audioId, it.ep);
            }
        }
    }


    override fun setDefaultFragment(): Fragment {
        return SplashFragment();
//        return TimerFragment();
    }

    override fun onDestroy() {
        PlayAudioManager.killMediaPlayer()
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }


}