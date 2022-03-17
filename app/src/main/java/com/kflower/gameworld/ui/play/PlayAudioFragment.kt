package com.kflower.gameworld.ui.play

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.util.Log
import android.widget.SeekBar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.PlayAudioFragmentBinding
import com.kflower.gameworld.model.AudioBook
import android.graphics.drawable.Drawable

import android.graphics.Bitmap

import android.content.Intent
import android.os.*

import androidx.core.content.ContextCompat
import com.kflower.gameworld.services.MediaSessionService

import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.adapter.AudioEpAdapter
import com.kflower.gameworld.MyApplication.Companion.fetchAudio
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import android.os.Environment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.currentMediaPos
import com.kflower.gameworld.bottomsheet.BottomSheetEpisodes
import com.kflower.gameworld.common.lifecycleOwner
import com.kflower.gameworld.ui.timer.TimerFragment
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getDrawable
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.addNewDownload
import com.kflower.gameworld.MyApplication.Companion.downloadTable
import com.kflower.gameworld.MyApplication.Companion.listDownloading
import com.kflower.gameworld.common.Key
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.model.DownloadAudio
import java.lang.Exception


class PlayAudioFragment(val item: AudioBook) : BaseFragment() {


    private lateinit var viewModel: PlayAudioViewModel;

    private var isChanging = false;
    lateinit var binding: PlayAudioFragmentBinding;
    lateinit var bottomSheet: BottomSheetEpisodes;
    lateinit var episodes: MutableList<String>;
    var downloadId: String? = null;
    var isDownloaded = false;

    override fun onResume() {
        super.onResume()
        binding.viewModel?.currentPos?.postValue(currentMediaPos.value)
        checkDownloadState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PlayAudioFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(PlayAudioViewModel::class.java)

        episodes = mutableListOf()

        for (i in 1..item.episodesAmount) {
            episodes.add(String.format(item.baseEpisode, i))
        }

        bottomSheet = BottomSheetEpisodes(episodes, object : AudioEpAdapter.AudioEpListener {
            override fun onClick(audioUrl: String, position: Int) {
//                viewModel?.isLoading?.postValue(true)
                mediaPlayer.seekTo(position, 0);
                bottomSheet.dismiss()
            }

            override fun onDownload(audioUrl: String, position: Int) {
            }

        })
        viewModel.item.postValue(item);

        binding.lifecycleOwner = this;
        binding.viewModel = viewModel;
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        //

        MyApplication.mAppContext?.lifecycleOwner()?.let {
            MyApplication.mIsPlaying.observe(it, { isPlaying ->
                viewModel.isPlaying.postValue(isPlaying)

                if (isPlaying) {
                    if (!PlayAudioManager.isShowNoti) {
                        ContextCompat.startForegroundService(
                            requireContext(),
                            Intent(requireContext(), MediaSessionService::class.java)
                        )
                        PlayAudioManager.isShowNoti = true;
                    }
                }
            })
            MyApplication.mIsLoading.observe(it, { isLoading ->
                viewModel.isLoading.postValue(isLoading)
            })
            MyApplication.currentMediaPos.observe(it, { curPos ->
                viewModel.currentPos.postValue(curPos)
            })
            MyApplication.mPlaybackState.observe(it, {
                if (mediaPlayer.duration > 0) {
                    viewModel.duration.postValue(mediaPlayer.duration);
                }
                if (!PlayAudioManager.isShowNoti) {
                    ContextCompat.startForegroundService(
                        requireContext(),
                        Intent(requireContext(), MediaSessionService::class.java)
                    )
                    PlayAudioManager.isShowNoti = true;
                }
            })
            MyApplication.mMediaItem.observe(it, {
                checkDownloadState();
                viewModel?.currentPart?.postValue("Tap ${mediaPlayer.currentMediaItemIndex + 1}");
                viewModel?.reset()
                if (mediaPlayer.duration > 0) {
                    viewModel.duration.postValue(mediaPlayer.duration);
                }

            })

            listDownloading.observe(this, { download ->
                if (download.size == 0 || download == null) {
                    binding.inDownloadLayout.visibility = View.GONE;
                    binding.imgDownload.visibility = View.VISIBLE;
                    downloadId = null;
                }
                download?.forEach { downloadIt ->
                    if (downloadIt.audioId == item.id &&
                        downloadIt.ep == mediaPlayer.currentMediaItemIndex
                    ) {
                        binding.inDownloadLayout.visibility = View.VISIBLE;
                        binding.imgDownload.visibility = View.GONE;
                        binding.downloadProgressBar.progress = downloadIt.progress
                        downloadId = downloadIt.id;
                    } else {
                        binding.inDownloadLayout.visibility = View.GONE;
                        binding.imgDownload.visibility = View.VISIBLE;
                        downloadId = null;
                    }
                }
                checkDownloadState();
            })
        }

        Glide.with(this)
            .asBitmap()
            .load(item.thumbnailUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.imgThumbnail.setImageBitmap(resource)
                    PlayAudioManager.thumnailBitmap = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        if (item.id != PlayAudioManager.playingAudio?.id) {
            MyApplication.isAbleToUpdateCurEp = false;
            PlayAudioManager.playingAudio = item
            PlayAudioManager.preparePlayNewAudioList(
                episodes
            )
            var result = MyApplication.audioTable.findAudio(item.id)

            Log.d(TAG, "onCreate: " + result[0].curEp)
            mediaPlayer.playWhenReady = true

            if (result.size > 0) {
                var audioItem = result[0];
                Log.d(TAG, "seeking: ");
                mediaPlayer.seekTo(audioItem.curEp, audioItem.progress)
            }


            //
            val sharedPref: SharedPreferences =
                requireContext().getSharedPreferences(Key.KEY_STORE, MODE_PRIVATE)

            val editor = sharedPref.edit()
            editor.putString(Key.KEY_PLAYING_AUDIO_ID, item.id)
            editor.commit()

        } else {
            checkDownloadState();
            viewModel.isPlaying.postValue(mediaPlayer.isPlaying)
            viewModel.duration.postValue(mediaPlayer.duration)
            viewModel.currentPart?.postValue("Tap ${mediaPlayer.currentMediaItemIndex + 1}");
            if (mediaPlayer.isPlaying) {
                if (!PlayAudioManager.isShowNoti) {
                    ContextCompat.startForegroundService(
                        requireContext(),
                        Intent(requireContext(), MediaSessionService::class.java)
                    )
                    PlayAudioManager.isShowNoti = true;
                }

            }
        }


        binding.apply {
            imgPausePlay.setOnClickListener {
                if (viewModel?.isPlaying?.value!!) {
                    mediaPlayer.pause()
                } else {
                    mediaPlayer.play()
                }
            }

            imgClock.setOnClickListener {
                navigateTo(TimerFragment());
            }

            imgStopDownload.setOnClickListener {
                try {
                    Log.d(TAG, "imgStopDownload: ")
                    fetchAudio.cancel(downloadId!!.toInt())
                } catch (e: Exception) {
                    Log.d(TAG, "imgStopDownload Error: " + e.message)
                    e.printStackTrace()
                }
            }

            imgNext.setOnClickListener {
                mediaPlayer.seekToNextMediaItem()
                mediaPlayer.playWhenReady = true
                if (mediaPlayer.currentMediaItemIndex != (PlayAudioManager.playingAudio?.episodesAmount!! - 1)) {
                    viewModel?.reset()
                } else {
                    mediaPlayer.seekTo(0, 0)
                }

            }


            imgPre.setOnClickListener {
                mediaPlayer.seekToPreviousMediaItem()
                mediaPlayer.playWhenReady = true
                if (mediaPlayer.currentMediaItemIndex != 0) {
                    viewModel?.reset()
                } else {
                    mediaPlayer.seekTo(0)
                }
            }
            imgFoward.setOnClickListener {
                viewModel!!.currentPos.apply {
                    var pos = value!!.plus(mediaPlayer.seekForwardIncrement)
                    if (pos!! >= viewModel?.duration!!.value!!) {
                        pos = viewModel?.duration!!.value!!
                    }
                    postValue(pos)
                    mediaPlayer.seekTo(pos)
                    mediaPlayer.playWhenReady = true
                }

            }
            imgRewind.setOnClickListener {
                viewModel!!.currentPos.apply {
                    var pos = value!!.minus(mediaPlayer.seekForwardIncrement)
                    if (pos!! <= 0L) {
                        pos = 0
                    }
                    postValue(pos)
                    mediaPlayer.seekTo(pos)
                    mediaPlayer.playWhenReady = true
                }
            }
            seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        var progress =
                            viewModel?.duration?.value!!.toFloat() * ((p1.toFloat() / 100))
                        val ipInt = (progress as Number).toLong()

                        if (p2) {
                            viewModel?.currentPos?.postValue(ipInt)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                        isChanging = true;
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                        var progress =
                            viewModel?.duration?.value!!.toFloat() * ((p0?.progress!!.toFloat() / 100))
                        val ipInt = (progress as Number).toLong()
                        mediaPlayer.seekTo(ipInt)
                        mediaPlayer.playWhenReady = true
                        isChanging = false;
                    }
                }
            )
        }

        binding.imgList.setOnClickListener {
            try {
                bottomSheet.show(parentFragmentManager, BottomSheetEpisodes.TAG);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.imgDownload.setOnClickListener {

            if (isDownloaded) {
                Toast.makeText(
                    requireContext(),
                    "Bạn đã tải tập truyện này rồi!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (checkSelfPermission(
                        requireContext(),
                        WRITE_EXTERNAL_STORAGE
                    ) !==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(WRITE_EXTERNAL_STORAGE),
                            1
                        )
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(WRITE_EXTERNAL_STORAGE), 1
                        )
                    }
                } else {
                    val file = "${item.id}_ID_${mediaPlayer.currentMediaItemIndex + 1}.mp3"
                    val downloadsPath =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                    val dirPath = "$downloadsPath/TopTopFiles/$file"

                    val request = Request(episodes[mediaPlayer.currentMediaItemIndex], dirPath)
                    request.priority = Priority.HIGH
                    request.networkType = NetworkType.ALL

                    fetchAudio.enqueue(request, { updatedRequest ->
                        var downloadItem = DownloadAudio(
                            id = updatedRequest.id.toString(),
                            audioId = item.id,
                            uri = dirPath,
                            ep = mediaPlayer.currentMediaItemIndex,
                        )
                        addNewDownload(downloadItem)

                    }) { error ->

                        Log.d("KHOA", "downloadError" + error)
                    }

                }

            }
        }

    }

    private fun checkDownloadState() {

        binding.inDownloadLayout.visibility = View.GONE;
        binding.imgDownload.visibility = View.VISIBLE;

        var result =
            downloadTable.findDownloadByAudioId(item.id, mediaPlayer.currentMediaItemIndex);
        if (result != null) {
            when (result.state) {
                DownloadState.COMPLETED -> {
                    isDownloaded = true;
                    context?.apply {
                        binding.imgDownload.setImageDrawable(
                            getDrawable(
                                this,
                                com.kflower.gameworld.R.drawable.ic_check
                            )
                        )
                    }
                    downloadId = null;
                }
                DownloadState.DOWNLOADING -> {
                    binding.inDownloadLayout.visibility = View.VISIBLE;
                    binding.imgDownload.visibility = View.GONE;
                    binding.downloadProgressBar.progress = result.progress
                    downloadId = result.id;
                }
                else -> {
                    binding.inDownloadLayout.visibility = View.GONE;
                    binding.imgDownload.visibility = View.VISIBLE;
                    downloadId = null;
                }
            }
        } else {
            context?.apply {
                binding.imgDownload.setImageDrawable(
                    getDrawable(
                        this,
                        com.kflower.gameworld.R.drawable.ic_download
                    )
                )
            }
            isDownloaded = false;
        }
    }


    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }


    override fun onDestroy() {
//        listener= null
        super.onDestroy()
    }

}