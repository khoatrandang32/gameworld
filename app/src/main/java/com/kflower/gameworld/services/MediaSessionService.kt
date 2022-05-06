package com.kflower.gameworld.services

import android.app.Service
import android.content.Intent

import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayer

import com.kflower.gameworld.common.MediaNotificationManager

import com.kflower.gameworld.common.PlayAudioManager
import android.support.v4.media.session.PlaybackStateCompat

import android.support.v4.media.MediaMetadataCompat
import android.view.KeyEvent
import android.util.Log
import com.google.android.exoplayer2.Player
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.mIsPlaying
import com.kflower.gameworld.MyApplication.Companion.mPlaybackState
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.common.lifecycleOwner
import com.kflower.gameworld.common.toBitMap


class MediaSessionService : Service() {
    val TAG = "MediaSessionService"
    val NOTIFICATION_ID = 888
    private var mMediaNotificationManager: MediaNotificationManager? = null
    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        mMediaNotificationManager = MediaNotificationManager(this)
            mediaSession = MediaSessionCompat(this, "ExoPlayer")
        mediaSession.isActive = true;

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                mediaPlayer.play()
            }

            override fun onPause() {
                mediaPlayer.pause()
            }

            override fun onSeekTo(pos: Long) {
                mediaPlayer.seekTo(pos)
            }
        })
        mIsPlaying.observeForever {
            startNotification()
        }
        mPlaybackState.observeForever {
            startNotification()

        }
        MyApplication.playerState.observeForever {
            if(it== Player.STATE_ENDED){
                mediaPlayer.seekTo(0,0);
            }
        }
        MyApplication.mMediaItem.observeForever {
            startNotification()
        }

        startNotification()

    }

    private fun getMetadata(): MediaMetadataCompat {

        val builder = MediaMetadataCompat.Builder()
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, "${PlayAudioManager.playingAudio?.title} - Tập ${mediaPlayer.currentMediaItemIndex+1}")
        builder.putLong(
            MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.duration
        )
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Tập ${mediaPlayer.currentMediaItemIndex+1}")
        builder.putBitmap(
            MediaMetadataCompat.METADATA_KEY_ALBUM_ART, PlayAudioManager.playingAudio?.imgBase64?.toBitMap()
        )

        return builder.build()
    }


    private fun getState(): PlaybackStateCompat {
        val state =
            if (mediaPlayer.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        val stateBuilder = PlaybackStateCompat.Builder()
        stateBuilder.setActions( PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                or PlaybackStateCompat.ACTION_SEEK_TO)
        stateBuilder.setBufferedPosition(mediaPlayer.duration)
        stateBuilder.setState(
            state,
            mediaPlayer.currentPosition,
            1.0f,
            SystemClock.elapsedRealtime()
        )
        return stateBuilder.build()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroyx: ")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if ("android.intent.action.MEDIA_BUTTON" == intent.action) {
            val keyEvent: KeyEvent? = intent.extras!!["android.intent.extra.KEY_EVENT"] as KeyEvent?
            Log.d(TAG, "onStartCommand: "+keyEvent?.keyCode)
            if (keyEvent?.keyCode === KeyEvent.KEYCODE_MEDIA_PAUSE) {
                mediaPlayer.pause()

            } else if(
                keyEvent?.keyCode === KeyEvent.KEYCODE_MEDIA_PLAY
            ) {
                mediaPlayer.play()
            }
            else if(
                keyEvent?.keyCode === KeyEvent.KEYCODE_MEDIA_NEXT
            ) {
                mediaPlayer.seekToNextMediaItem()
            }
            else if(
                keyEvent?.keyCode === KeyEvent.KEYCODE_MEDIA_PREVIOUS
            ) {
                mediaPlayer.seekToPreviousMediaItem()
            }
            else if(
                keyEvent?.keyCode === KeyEvent.KEYCODE_MEDIA_STOP
            ) {
                mediaPlayer.stop()
                PlayAudioManager.isShowNoti=false;
                stopSelf()
                return super.onStartCommand(intent, flags, startId)
            }
            startNotification()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun startNotification(){
        val metadata= getMetadata()
        val state= getState()

        mediaSession.setMetadata(metadata);
        mediaSession.setPlaybackState(state);

        val notification= mMediaNotificationManager?.getNotification(
            metadata,
            state,
            mediaSession.sessionToken
        )

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }



}