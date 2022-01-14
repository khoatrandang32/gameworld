package com.kflower.gameworld.services

import android.app.Notification
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.util.Util
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.CHANNEL_ID
import com.kflower.gameworld.R
import org.json.JSONObject
import java.lang.Exception

class AppDownloadService: DownloadService(1, DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID, R.string.app_name, R.string.app_name){

    private lateinit var notificationHelper: DownloadNotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper= DownloadNotificationHelper(this, CHANNEL_ID)

    }
    override fun getDownloadManager(): DownloadManager {
        val manager = MyApplication.downloadManager
        manager.maxParallelDownloads = 3
        manager.addListener(object : DownloadManager.Listener {
            override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
                 Toast.makeText(this@AppDownloadService, "Deleted", Toast.LENGTH_SHORT).show()
                Log.d("KHOA", "onDownloadRemoved: ")
            }

            override fun onDownloadsPausedChanged(downloadManager: DownloadManager, downloadsPaused: Boolean) {
                if (downloadsPaused){
                    Toast.makeText(this@AppDownloadService, "paused", Toast.LENGTH_SHORT).show()
                    Log.d("KHOA", "downloadsPaused: 1")

                } else{
                    Toast.makeText(this@AppDownloadService, "resumed", Toast.LENGTH_SHORT).show()
                    Log.d("KHOA", "downloadsPaused: 2")
                }

            }

            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?
            ) {
                Log.d("KHOA", "onDownloadChanged:")

                super.onDownloadChanged(downloadManager, download, finalException)
            }
        })
        // return (application as App).appContainer.downloadManager
        return manager
    }

    //If you want to restart the download when it failed, you the can override this method, it uses Jobscheduler.
    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return notificationHelper.buildProgressNotification(this,R.drawable.ic_app_logo, null, getString(R.string.app_name), downloads);
    }

}