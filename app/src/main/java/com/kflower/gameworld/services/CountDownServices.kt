package com.kflower.gameworld.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock


class CountDownServices : Service() {
    var bi = Intent(COUNTDOWN_BR)
    var time= 21000L;
    private lateinit var cdt: CountDownTimer

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        cdt?.cancel()
        Log.i(TAG, "Timer cancelled")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            time = intent.getLongExtra(TIME_KEY, 0);
        }
        Log.i(TAG, "Starting timer...")
        cdt = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000)
                bi.putExtra("countdown", millisUntilFinished)
                sendBroadcast(bi)
            }

            override fun onFinish() {
                Log.i(TAG, "Timer finished")
            }
        }
        cdt.start()

        return super.onStartCommand(intent, flags, startId)
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
        public var  TIME_KEY= "TIME_KEY";

    }
}