package com.kflower.gameworld.common

import android.app.*

import android.content.Intent

import com.kflower.gameworld.MainActivity

import android.os.Build

import androidx.annotation.RequiresApi

import android.support.v4.media.session.PlaybackStateCompat

import androidx.media.session.MediaButtonReceiver

import android.graphics.Color

import androidx.core.content.ContextCompat

import android.support.v4.media.MediaDescriptionCompat

import android.support.v4.media.session.MediaSessionCompat

import androidx.annotation.NonNull

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kflower.gameworld.MyApplication.Companion.CHANNEL_ID
import com.kflower.gameworld.R
import com.kflower.gameworld.services.MediaSessionService


class MediaNotificationManager(musicContext: MediaSessionService) {
    private val mService: MediaSessionService
    private val mPlayAction: NotificationCompat.Action
    private val mPauseAction: NotificationCompat.Action
    private val mNextAction: NotificationCompat.Action
    private val mPreAction: NotificationCompat.Action
    private val mCloseAction: NotificationCompat.Action
    val notificationManager: NotificationManager

    fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

    fun getNotification(
        metadata: MediaMetadataCompat,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token
    ): Notification {
        val isPlaying = state.state == PlaybackStateCompat.STATE_PLAYING
        val description = metadata.description
        val builder: NotificationCompat.Builder =
            buildNotification(state, token, isPlaying, description)
        return builder.build()
    }

    private fun buildNotification(
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token,
        isPlaying: Boolean,
        description: MediaDescriptionCompat
    ): NotificationCompat.Builder {

        // Create the (mandatory) notification channel when running on Android Oreo.
        if (isAndroidOOrHigher) {
            createChannel()
        }
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(mService, CHANNEL_ID)
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(token)
                .setShowActionsInCompactView(0) // For backwards compatibility with Android L and earlier.
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        mService,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
        )
            .setColor(ContextCompat.getColor(mService, R.color.main_color))
            .setLargeIcon(description.iconBitmap)
            .setSmallIcon(R.drawable.ic_play) // Pending intent that is fired when user clicks on notification.
            .setContentIntent(createContentIntent()) // Title - Usually Song name.
            .setContentTitle(description.title) // When notification is deleted (when playback is paused and notification can be
            // deleted) fire MediaButtonPendingIntent with ACTION_PAUSE.
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    mService, PlaybackStateCompat.ACTION_PAUSE
                )
            )
        builder.addAction(mPreAction)
        builder.addAction(if (isPlaying) mPauseAction else mPlayAction)
        builder.addAction(mNextAction)
        builder.addAction(mCloseAction)

        return builder
    }

    // Does nothing on versions of Android earlier than O.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            val name: CharSequence = "MediaSession"
            // The user-visible description of the channel.
            val description = "MediaSession and MediaPlayer"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)
            Log.d(TAG, "createChannel: New channel created")
        } else {
            Log.d(TAG, "createChannel: Existing channel reused")
        }
    }

    private val isAndroidOOrHigher: Boolean
        private get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    private fun createContentIntent(): PendingIntent {
        val openUI = Intent(mService, MainActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            mService, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    companion object {
        const val NOTIFICATION_ID = 412
        private val TAG = MediaNotificationManager::class.java.simpleName
        private const val REQUEST_CODE = 501
    }

    init {
        mService = musicContext
        notificationManager = mService.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        mPlayAction = NotificationCompat.Action(
            R.drawable.ic_play,
            "play",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                mService,
                PlaybackStateCompat.ACTION_PLAY
            )
        )
        mPauseAction = NotificationCompat.Action(
            R.drawable.ic_pause,
            "pause",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                mService,
                PlaybackStateCompat.ACTION_PAUSE
            )
        )
        mNextAction = NotificationCompat.Action(
            R.drawable.ic_next_solid,
            "next",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                mService,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )

        mPreAction = NotificationCompat.Action(
            R.drawable.ic_prev_solid,
            "pre",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                mService,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )

        mCloseAction = NotificationCompat.Action(
            R.drawable.ic_close,
            "close",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                mService,
                PlaybackStateCompat.ACTION_STOP
            )
        )
        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        notificationManager.cancelAll()
    }
}