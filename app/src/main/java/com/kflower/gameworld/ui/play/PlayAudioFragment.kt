package com.kflower.gameworld.ui.play

import android.util.Log
import android.widget.SeekBar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.PlayAudioFragmentBinding
import com.kflower.gameworld.model.AudioBook
import android.graphics.drawable.Drawable

import android.graphics.Bitmap

import android.content.Intent
import android.os.*

import androidx.core.content.ContextCompat
import com.kflower.gameworld.interfaces.isMediaPlayChanged
import com.kflower.gameworld.services.MediaSessionService

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.kflower.gameworld.MyApplication.Companion.listener
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.adapter.AudioEpAdapter
import com.kflower.gameworld.MyApplication.Companion.fetchAudio
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import android.os.Environment
import com.kflower.gameworld.common.Utils


class PlayAudioFragment(val item: AudioBook) : BaseFragment() {


    private lateinit var viewModel: PlayAudioViewModel;

    private lateinit var durationHandler: Handler
    private var isChanging = false;
    lateinit var binding: PlayAudioFragmentBinding;
    lateinit var sheetBehavior: BottomSheetBehavior<*>
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("KHOA", "onCreate: ")

        binding = PlayAudioFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(PlayAudioViewModel::class.java)

        viewModel.item.postValue(item);
        durationHandler = Handler(Looper.getMainLooper());

        binding.lifecycleOwner = this;
        binding.viewModel = viewModel;
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        //

        gridLayoutManager =
            GridLayoutManager(requireContext(), 3)

        binding.bottomSheetLayout?.apply {
            recyclerView.layoutManager=gridLayoutManager;
            recyclerView.adapter= AudioEpAdapter(requireContext(), item.episodes, object :AudioEpAdapter.AudioEpListener{
                override fun onClick(audioUrl: String, position: Int) {
                    viewModel?.isLoading?.postValue(true)
                    mediaPlayer.seekTo(position,0);
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                }

                override fun onDownload(audioUrl: String, position: Int) {
                }


            });

        }

        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet);

        Glide.with(this)
            .asBitmap()
            .load(item.thumbnailUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.imgThumbnail.setImageBitmap(resource)
                    PlayAudioManager.thumnailBitmap= resource
                    println("KHOA${Utils().bitMapToString(resource)}")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        if(item.id!=PlayAudioManager.playingAudio?.id){
            PlayAudioManager.preparePlayNewAudioList(
                item.episodes)
            PlayAudioManager.playingAudio= item
            mediaPlayer.playWhenReady = true

        }
        else{
            viewModel.isPlaying.postValue(mediaPlayer.isPlaying)
            viewModel.duration.postValue(mediaPlayer.duration)
            viewModel.currentPos.postValue(mediaPlayer.currentPosition)
            viewModel.currentPart?.postValue("Tap ${mediaPlayer.currentMediaItemIndex+1}");
            viewModel.isLoading.postValue(false)

            if (mediaPlayer.isPlaying) {
                if (!PlayAudioManager.isShowNoti) {
                    ContextCompat.startForegroundService(
                        requireContext(),
                        Intent(requireContext(), MediaSessionService::class.java)
                    )
                    PlayAudioManager.isShowNoti = true;
                }
                activity?.runOnUiThread(updateSeekBarTime)

            } else {
                durationHandler.removeCallbacks(updateSeekBarTime);

            }
        }


        listener= object : isMediaPlayChanged {
            override fun isPlayChanged(isPlaying: Boolean) {
                viewModel.isPlaying.postValue(isPlaying)

                if (isPlaying) {
                    if (!PlayAudioManager.isShowNoti) {
                        ContextCompat.startForegroundService(
                            requireContext(),
                            Intent(requireContext(), MediaSessionService::class.java)
                        )
                        PlayAudioManager.isShowNoti = true;
                    }
                    activity?.runOnUiThread(updateSeekBarTime)

                } else {
                    durationHandler.removeCallbacks(updateSeekBarTime);

                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                viewModel.isLoading.postValue(false)
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
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                viewModel?.currentPart?.postValue("Tap ${mediaPlayer.currentMediaItemIndex+1}");
                viewModel?.reset()
                if (mediaPlayer.duration > 0) {
                    viewModel.duration.postValue(mediaPlayer.duration);
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
            imgNext.setOnClickListener {
                mediaPlayer.seekToNextMediaItem()
                if (mediaPlayer.currentMediaItemIndex != (PlayAudioManager.playingAudio?.episodes?.size!!-1)) {
                    viewModel?.reset()
                } else {
                    mediaPlayer.seekTo(0,0)
                    viewModel?.currentPos?.postValue(0)
                }

                viewModel?.isLoading?.postValue(true)
            }

            bottomSheetLayout.imgClose.setOnClickListener {
                sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            imgPre.setOnClickListener {
                mediaPlayer.seekToPreviousMediaItem()
                if (mediaPlayer.currentMediaItemIndex != 0) {
                    viewModel?.reset()
                } else {
                    mediaPlayer.seekTo(0)
                    viewModel?.currentPos?.postValue(0)
                }
                viewModel?.isLoading?.postValue(true)
            }
            imgFoward.setOnClickListener {
                viewModel?.isLoading?.postValue(true)
                viewModel!!.currentPos.apply {
                    var pos = value!!.plus(mediaPlayer.seekForwardIncrement)
                    if (pos!! >= viewModel?.duration!!.value!!) {
                        pos = viewModel?.duration!!.value!!
                    }
                    postValue(pos)
                    mediaPlayer.seekTo(pos)
                }

            }
            imgRewind.setOnClickListener {
                viewModel?.isLoading?.postValue(true)
                viewModel!!.currentPos.apply {
                    var pos = value!!.minus(mediaPlayer.seekForwardIncrement)
                    if (pos!! <= 0L) {
                        pos = 0
                    }
                    postValue(pos)
                    mediaPlayer.seekTo(pos)
                }
            }
            seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        var progress =
                            viewModel?.duration?.value!!.toFloat() * ((p1.toFloat() / 100))
                        val ipInt = (progress as Number).toLong()

                        if (p2) {
                            viewModel?.currentPos?.postValue(ipInt);
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
                        viewModel?.isLoading?.postValue(true)
                        isChanging = false;
                    }
                }
            )
        }

        binding.imgList.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            gridLayoutManager.scrollToPositionWithOffset(mediaPlayer.currentMediaItemIndex,500);
        }
        binding.imgDownload.setOnClickListener {
            val file = "${item.id}_ID_${mediaPlayer.currentMediaItemIndex+1}.mp3"
            val downloadsPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
            val dirPath = "$downloadsPath/FileDownloader/$file"

            val request = Request(item.episodes[mediaPlayer.currentMediaItemIndex], dirPath)
            request.priority= Priority.HIGH
            request.networkType= NetworkType.ALL
//            request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")

            fetchAudio.enqueue(request, { updatedRequest ->
                Log.d("KHOA", "onCreate: "+updatedRequest)
            }) { error ->

                Log.d("KHOA", "onCreate: "+error)
            }


//            var downloadAudio= DownloadAudio().execute(item.episodes[mediaPlayer.currentMediaItemIndex]);
        }

        sheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private val updateSeekBarTime: Runnable = object : Runnable {
        override fun run() {
            if (!isChanging) {
                viewModel.currentPos.postValue(mediaPlayer.currentPosition)
            }
            durationHandler.postDelayed(this, 1000)
        }
    }


    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

    override fun onDestroy() {
        listener= null
        super.onDestroy()
    }

}