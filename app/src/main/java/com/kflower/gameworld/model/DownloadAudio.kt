package com.kflower.gameworld.model

import com.kflower.gameworld.enum.DownloadState

data class DownloadAudio(
    var id: String,
    var audioId: String,
    var progress: Int = 0,
    var uri: String,
    var state: DownloadState= DownloadState.DOWNLOADING,
    var ep: Int,
)