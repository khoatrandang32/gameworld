package com.kflower.gameworld

import android.app.Activity
import android.app.Application
import android.util.Log
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.FirebaseApp
import com.kflower.gameworld.interfaces.isMediaPlayChanged
import com.tonyodev.fetch2.Download
import glimpse.core.Glimpse
import java.io.File
import java.util.concurrent.Executor
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.Fetch.Impl.getInstance

import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import java.lang.Error
import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.database.AppDatabaseHelper
import com.kflower.gameworld.database.AudioTable
import com.kflower.gameworld.database.HomeCateTable
import com.kflower.gameworld.interfaces.TimerChange
import com.kflower.gameworld.model.AudioGroup
import com.kflower.gameworld.services.CountDownServices


class MyApplication : Application() {
    companion object {
        var TAG = "KHOA"
        var isSettingUp = true
        var mAppContext: Context? = null;
        var timeCountDown: Long = 0
        var curTimer = MutableLiveData<Long>(0);
        var mIsLoading = MutableLiveData(false);
        var mIsPlaying = MutableLiveData(false);
        var mPlaybackState = MutableLiveData(0);
        var currentMediaPos = MutableLiveData(0L);
        var mMediaItem = MutableLiveData<MediaItem>();
        lateinit var db: SQLiteDatabase
        lateinit var audioTable: AudioTable
        lateinit var homeCateTable: HomeCateTable

        //
        lateinit var simpleCache: SimpleCache
        lateinit var downloadCache: SimpleCache
        lateinit var cacheFolder: File
        lateinit var fetchAudio: Fetch
        private lateinit var durationHandler: Handler

        const val exoPlayerCacheSize: Long = 90 * 1024 * 1024
        lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
        lateinit var exoDatabaseProvider: ExoDatabaseProvider
        lateinit var cacheDataSourceFactory: DataSource.Factory
        lateinit var mediaPlayer: ExoPlayer
        lateinit var downloadManager: DownloadManager
        const val CHANNEL_ID = "Kflower"
        lateinit var httpDataSourceFactory: DefaultHttpDataSource.Factory;

        //
        var timeIntent: Intent? = null;
        lateinit var databaseProvider: DatabaseProvider
        lateinit var downloadContentDirectory: File

        public fun startTimer(time: Long, context: Context) {
            if (timeIntent != null) {
                (context as Activity).stopService(timeIntent)
            }
            timeCountDown = time
            timeIntent = Intent(context, CountDownServices::class.java);
            timeIntent?.putExtra(CountDownServices.TIME_KEY, time)
            (context as Activity).startService(timeIntent)
        }

    }

    override fun onCreate() {
        super.onCreate()

        db = AppDatabaseHelper(this).writableDatabase;
        audioTable = AudioTable(db);
        homeCateTable = HomeCateTable(db);

        durationHandler = Handler(Looper.getMainLooper());

        val br: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent!!.extras != null) {
                    val millisUntilFinished = intent.getLongExtra("countdown", 0)
                    curTimer.postValue(millisUntilFinished)

                }
            }
        }
        registerReceiver(br, IntentFilter(CountDownServices.COUNTDOWN_BR));

        Glimpse.init(this)
        FirebaseApp.initializeApp(this);
        val fetchConfiguration: FetchConfiguration = FetchConfiguration.Builder(this)
            .setDownloadConcurrentLimit(3)
            .enableLogging(true)
            .build()

        fetchAudio = getInstance(fetchConfiguration)

        val fetchListener: FetchListener = object : FetchListener {
            override fun onAdded(download: Download) {
                Log.d("KHOA", "onAdded: ")
            }

            override fun onCancelled(download: Download) {
                Log.d("KHOA", "onCancelled: ")
            }

            override fun onCompleted(download: Download) {
                Log.d("KHOA", "onCompleted: ")
            }

            override fun onDeleted(download: Download) {
                Log.d("KHOA", "onDeleted: ")
            }

            override fun onDownloadBlockUpdated(
                download: Download,
                downloadBlock: DownloadBlock,
                totalBlocks: Int
            ) {
                Log.d("KHOA", "onDownloadBlockUpdated: ")
            }

            override fun onError(
                download: Download,
                error: com.tonyodev.fetch2.Error,
                throwable: Throwable?
            ) {
                Log.d("KHOA", "onError: ")
            }

            override fun onPaused(download: Download) {
                Log.d("KHOA", "onPaused: ")
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {

                Log.d(
                    "KHOA",
                    "onProgress: " + etaInMilliSeconds + " - " + downloadedBytesPerSecond + " - " + download.progress + " - " + download.total + " - " + download.id
                )
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                Log.d("KHOA", "onQueued: ")
            }

            override fun onRemoved(download: Download) {
                Log.d("KHOA", "onRemoved: ")
            }

            override fun onResumed(download: Download) {
                Log.d("KHOA", "onResumed: ")
            }

            override fun onStarted(
                download: Download,
                downloadBlocks: List<DownloadBlock>,
                totalBlocks: Int
            ) {
                Log.d("KHOA", "onStarted: ")
            }

            override fun onWaitingNetwork(download: Download) {
                Log.d("KHOA", "onWaitingNetwork: ")
            }

        }
        fetchAudio.addListener(fetchListener);

        cacheFolder = File(cacheDir, "audio")
        leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        exoDatabaseProvider = ExoDatabaseProvider(this)
        simpleCache = SimpleCache(cacheFolder, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
        //
        databaseProvider = StandaloneDatabaseProvider(this)
        downloadContentDirectory = File(getExternalFilesDir(null), "KFlower")
        Log.d("KHOA", "onCreate: 1" + downloadContentDirectory.absolutePath)
        Log.d("KHOA", "onCreate: 2" + cacheFolder.absolutePath)
        downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), databaseProvider);
        //
        initMediaPlayer()
        initDownloadManager();

    }

    private fun initDownloadManager() {
        val downloadExecutor = Executor { obj: Runnable -> obj.run() }
        downloadManager = DownloadManager(
            this,
            databaseProvider,
            downloadCache,
            httpDataSourceFactory,
            downloadExecutor
        )

    }

    private fun initMediaPlayer() {
        cacheFolder.delete();
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        cacheDataSourceFactory = CacheDataSource.Factory().setCache(
            simpleCache
        )
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        mediaPlayer = ExoPlayer.Builder(applicationContext)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory)).build()
        mediaPlayer.addListener(object : Player.Listener {

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                currentMediaPos.postValue(mediaPlayer.currentPosition)
            }

            override fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {
                super.onAudioAttributesChanged(audioAttributes)
                Log.d(TAG, "onAudioAttributesChanged: ")
            }

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                super.onAudioSessionIdChanged(audioSessionId)
                Log.d(TAG, "onAudioSessionIdChanged: ")
            }

            override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
                super.onAvailableCommandsChanged(availableCommands)
                Log.d(TAG, "onAvailableCommandsChanged: ")
            }

            override fun onCues(cues: MutableList<Cue>) {
                super.onCues(cues)
                Log.d(TAG, "onCues: ")
            }

            override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
                super.onDeviceInfoChanged(deviceInfo)
                Log.d(TAG, "onDeviceInfoChanged: ")
            }

            override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
                super.onDeviceVolumeChanged(volume, muted)
                Log.d(TAG, "onDeviceVolumeChanged: ")
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                mIsLoading.postValue(isLoading)
                currentMediaPos.postValue(mediaPlayer.currentPosition)
                if (!isSettingUp){
                    mediaPlayer.playWhenReady = true
                }
                super.onIsLoadingChanged(isLoading)
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                Log.d(TAG, "onMediaMetadataChanged: ")
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.d(TAG, "onPlayerError: ")
            }

            override fun onRenderedFirstFrame() {
                super.onRenderedFirstFrame()
                Log.d(TAG, "onRenderedFirstFrame: ")
            }

            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                Log.d(TAG, "onPositionDiscontinuity: ")
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                super.onTracksChanged(trackGroups, trackSelections)
                Log.d(TAG, "onTracksChanged: ")
            }

            override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
                super.onTrackSelectionParametersChanged(parameters)
                Log.d(TAG, "onTrackSelectionParametersChanged: ")
            }

            override fun onVolumeChanged(volume: Float) {
                super.onVolumeChanged(volume)
                Log.d(TAG, "onVolumeChanged: ")
            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
                Log.d(TAG, "onTimelineChanged: ")
            }

            override fun onSkipSilenceEnabledChanged(skipSilenceEnabled: Boolean) {
                super.onSkipSilenceEnabledChanged(skipSilenceEnabled)
                Log.d(TAG, "onSkipSilenceEnabledChanged: ")
            }

            override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
                super.onSeekForwardIncrementChanged(seekForwardIncrementMs)
                Log.d(TAG, "onSeekForwardIncrementChanged: ")
            }

            override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
                super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason)
                Log.d(TAG, "onPlaybackSuppressionReasonChanged: ")
            }

            override fun onMetadata(metadata: Metadata) {
                super.onMetadata(metadata)
                Log.d(TAG, "onMetadata: ")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                mIsLoading.postValue(false)
                super.onIsPlayingChanged(isPlaying)
                mIsPlaying.postValue(isPlaying)
                if (isPlaying) {
                    isSettingUp= false;
                    (mAppContext as Activity)?.runOnUiThread(updateSeekBarTime)
                } else {
                    durationHandler.removeCallbacks(updateSeekBarTime);
                }

            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                currentMediaPos.postValue(mediaPlayer.currentPosition)
                mPlaybackState.postValue(playbackState)

            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.d(TAG, "onMediaItemTransition: ")
                var audio = PlayAudioManager.playingAudio
                audio?.let {
                    it.curEp = mediaPlayer.currentMediaItemIndex
                    audioTable.updateAudioEp(it)
                }

                super.onMediaItemTransition(mediaItem, reason)
                mediaItem?.let {
                    mMediaItem.postValue(it)
                }
            }

        })
    }

    private val updateSeekBarTime: Runnable = object : Runnable {
        override fun run() {
            currentMediaPos.postValue(mediaPlayer.currentPosition)
            PlayAudioManager.playingAudio?.let {
                var audio = it;
                audio.progress = mediaPlayer.currentPosition
                AudioTable(db).updateAudioProgress(audio)
            }

            durationHandler.postDelayed(this, 1000)

        }
    }

    override fun onTerminate() {
        Log.d(TAG, "onTerminate: ")
        super.onTerminate()
    }

}
