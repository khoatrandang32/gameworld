package com.kflower.gameworld.interfaces

import com.google.android.exoplayer2.MediaItem

interface isMediaPlayChanged {
    fun isPlayChanged(isPlaying: Boolean)
    fun onPlaybackStateChanged(playbackState: Int)
    fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int)
}
