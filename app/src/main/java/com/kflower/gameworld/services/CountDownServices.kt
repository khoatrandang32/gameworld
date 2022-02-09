package com.kflower.gameworld.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.app.AlarmManager
import android.app.Notification

import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import android.os.Build
import android.app.NotificationManager

import android.app.NotificationChannel
import android.graphics.Color
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.kflower.gameworld.MyApplication

import android.widget.RemoteViews
import com.kflower.gameworld.MyApplication.Companion.timeCountDown
import com.kflower.gameworld.R


class CountDownServices : Service() {
    var bi = Intent(COUNTDOWN_BR)
    var time = 0L;

    private lateinit var cdt: CountDownTimer

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startMyOwnForeground()
        else startForeground(1, Notification())
    }
    override fun onDestroy() {
        cdt?.cancel()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,
            MyApplication.CHANNEL_ID
        )
        val notification: Notification = notificationBuilder
            .setContentTitle("213123")
            .setContentText("AHihi")
            .build()
        startForeground(1, notification)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            time = intent.getLongExtra(TIME_KEY, 0);
        }
        cdt = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bi.putExtra("countdown", millisUntilFinished)
                sendBroadcast(bi)
            }

            override fun onFinish() {
                timeCountDown= 0L;
                MyApplication.mediaPlayer.pause()
                onDestroy()
            }
        }
        cdt.start()

        return START_STICKY
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmService =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
            restartServicePendingIntent
        super.onTaskRemoved(rootIntent)
    }

    companion object {
        private const val TAG = "BroadcastService"
        const val COUNTDOWN_BR = "your_package_name.countdown_br"
        public var TIME_KEY = "TIME_KEY";

    }
}