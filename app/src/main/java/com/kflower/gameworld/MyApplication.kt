package com.kflower.gameworld

import android.app.Activity
import android.app.Application
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
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
import com.kflower.gameworld.interfaces.TimerChange
import com.kflower.gameworld.services.CountDownServices


class MyApplication: Application(){
    companion object{
        lateinit var simpleCache: SimpleCache
        lateinit var downloadCache: SimpleCache
        lateinit var cacheFolder: File
        lateinit var fetchAudio: Fetch

        const val exoPlayerCacheSize: Long = 90 * 1024 * 1024
        lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
        lateinit var exoDatabaseProvider: ExoDatabaseProvider
        lateinit var cacheDataSourceFactory: DataSource.Factory
        lateinit var mediaPlayer: ExoPlayer
        lateinit var downloadManager: DownloadManager
        const val CHANNEL_ID = "Kflower"
        lateinit var httpDataSourceFactory:DefaultHttpDataSource.Factory;
        //
        var listenerNoti: isMediaPlayChanged? = null
        var listener: isMediaPlayChanged? = null
        var listenerTimer: TimerChange? = null
        var miniPlayerListener: isMediaPlayChanged? = null
        lateinit  var databaseProvider: DatabaseProvider
        lateinit var downloadContentDirectory: File

        public fun startTimer(time:Long, context:Context){
            var intent = Intent(context, CountDownServices::class.java);
            intent.putExtra(CountDownServices.TIME_KEY,time)
            (context as Activity).startService(intent)
        }

    }
    override fun onCreate() {
        super.onCreate()

        val br: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                listenerTimer?.onTimerChange(1)
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

                Log.d("KHOA", "onProgress: "+etaInMilliSeconds+" - "+downloadedBytesPerSecond+" - "+download.progress+" - "+download.total+" - "+download.id)
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
        databaseProvider= StandaloneDatabaseProvider(this)
        downloadContentDirectory = File(getExternalFilesDir(null), "KFlower")
        Log.d("KHOA", "onCreate: 1"+ downloadContentDirectory.absolutePath)
        Log.d("KHOA", "onCreate: 2"+ cacheFolder.absolutePath)
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
        mediaPlayer.addListener(object :Player.Listener{

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.d("KHOA", "KHOA onIsPlayingChanged: ")
                super.onIsPlayingChanged(isPlaying)
                listener?.isPlayChanged(isPlaying);
                listenerNoti?.isPlayChanged(isPlaying);
                miniPlayerListener?.isPlayChanged(isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                Log.d("KHOA", "KHOA onPlaybackStateChanged: ")

                super.onPlaybackStateChanged(playbackState)
                listener?.onPlaybackStateChanged(playbackState);
                listenerNoti?.onPlaybackStateChanged(playbackState);
                miniPlayerListener?.onPlaybackStateChanged(playbackState);
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.d("KHOA", "KHOA onMediaItemTransition: ")

                super.onMediaItemTransition(mediaItem, reason)
                listener?.onMediaItemTransition(mediaItem, reason)
                listenerNoti?.onMediaItemTransition(mediaItem, reason)
                miniPlayerListener?.onMediaItemTransition(mediaItem, reason)
            }

        })
    }



}
