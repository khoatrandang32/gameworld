package com.kflower.gameworld.common

import android.content.Context

import android.util.Log

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.kflower.gameworld.interfaces.isMediaPlayChanged
import android.graphics.Bitmap
import com.kflower.gameworld.MyApplication.Companion.cacheDataSourceFactory
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.model.AudioBook


object PlayAudioManager {
    private var listenerNoti: isMediaPlayChanged? = null
    private var listener: isMediaPlayChanged? = null
    public var thumnailBitmap: Bitmap?=null
    public var isShowNoti: Boolean=false
    public var playingAudio:AudioBook?=null;


    fun preparePlayNewAudioList( episodes: MutableList<String>) {

        var list = mutableListOf<MediaSource>()

        episodes.map {
            //
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(it))
            list.add(mediaSource)
        }
        //
        mediaPlayer.setMediaSources(list)
        mediaPlayer.prepare()
    }

    public fun killMediaPlayer() {
//        mediaPlayer.stop()

    }

}