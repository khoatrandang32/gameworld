package com.kflower.gameworld.bottomsheet

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.audioTable
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.adapter.AudioEpAdapter
import com.kflower.gameworld.adapter.TimeListAdapter
import com.kflower.gameworld.common.Key
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.lifecycleOwner
import com.kflower.gameworld.common.toBitMap
import com.kflower.gameworld.databinding.BottomSheetMediaPlayerBinding
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.services.MediaSessionService
import com.kflower.gameworld.ui.play.PlayAudioViewModel
import glimpse.glide.GlimpseTransformation


class BottomSheetMedia : BottomSheetDialogFragment {

    private lateinit var viewModel: PlayAudioViewModel;
    lateinit var binding: BottomSheetMediaPlayerBinding;

    private var isChanging = false;
    lateinit var bottomSheetEp: BottomSheetEpisodes;
    lateinit var item: AudioBook;
    lateinit var bottomSheetDownload: BottomSheetDownload;
    lateinit var episodes: MutableList<String>;

    constructor(item: AudioBook) {
        this.item= item
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.kflower.gameworld.R.style.SheetDialog);

        binding = BottomSheetMediaPlayerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(PlayAudioViewModel::class.java)

        episodes = mutableListOf()

        for (i in 1..item.episodesAmount) {
            episodes.add(String.format(item.baseEpisode, i))
        }
        bottomSheetDownload= BottomSheetDownload(episodes,item)
        bottomSheetEp = BottomSheetEpisodes(episodes, item, object : AudioEpAdapter.AudioEpListener {
            override fun onClick(audioUrl: String, position: Int) {
//                viewModel?.isLoading?.postValue(true)
                mediaPlayer.seekTo(position, 0);
                bottomSheetEp.dismiss()
            }

        })
        viewModel.item.postValue(item);

        binding.lifecycleOwner = this;
        binding.viewModel = viewModel;

        MyApplication.mAppContext?.lifecycleOwner()?.let {
            MyApplication.mIsPlaying.observe(it) { isPlaying ->
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
            }
            MyApplication.mIsLoading.observe(it) { isLoading ->
                viewModel.isLoading.postValue(isLoading)
            }
            MyApplication.currentMediaPos.observe(it) { curPos ->
                viewModel.currentPos.postValue(curPos)
            }
            MyApplication.mPlaybackState.observe(it) {
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
            MyApplication.playerState.observe(it) { playerState ->
                if (playerState == Player.STATE_ENDED) {
                    mediaPlayer.seekTo(0, 0);
                }
            }
            MyApplication.mMediaItem.observe(it) {
                viewModel?.currentPart?.postValue(mediaPlayer.currentMediaItemIndex + 1);
                viewModel?.reset()
                if (mediaPlayer.duration > 0) {
                    viewModel.duration.postValue(mediaPlayer.duration);
                }

            }

        }

//        Glide.with(this)
//            .asBitmap()
//            .load(item.thumbnailUrl)
//            .transform(GlimpseTransformation())
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    binding.imgThumbnail.setImageBitmap(resource)
//                    PlayAudioManager.thumnailBitmap = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//            })

        Glide.with(this).load(item.imgBase64.toBitMap())
            .transform(GlimpseTransformation())
            .into(binding.imgThumbnail);


        PlayAudioManager.thumnailBitmap = item.imgBase64.toBitMap();

        if (item.id != PlayAudioManager.playingAudio?.id) {
            MyApplication.isAbleToUpdateCurEp = false;
            PlayAudioManager.playingAudio = item
            PlayAudioManager.preparePlayNewAudioList(
                episodes
            )
            audioTable.updateAudioHistoryTime(requireContext(), item.id)

            var result = audioTable.findAudio(item.id)

            mediaPlayer.playWhenReady = true

            if (result.size > 0) {
                var audioItem = result[0];
                mediaPlayer.seekTo(audioItem.curEp, audioItem.progress)
            }


            //

            val sharedPref: SharedPreferences =
                requireContext().getSharedPreferences(Key.KEY_STORE, MODE_PRIVATE)

            val editor = sharedPref.edit()
            editor.putString(Key.KEY_PLAYING_AUDIO_ID, item.id)
            editor.commit()

        } else {
            viewModel.isPlaying.postValue(mediaPlayer.isPlaying)
            viewModel.duration.postValue(mediaPlayer.duration)
            viewModel.currentPart?.postValue(mediaPlayer.currentMediaItemIndex + 1);
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
                bottomSheetEp.show(parentFragmentManager, BottomSheetEpisodes.TAG);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.imgDownload.setOnClickListener {
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
            }
            else bottomSheetDownload.show(parentFragmentManager, BottomSheetDownload.TAG);
        }

        viewModel.isTimeSelect.observe(this) {
            TransitionManager.beginDelayedTransition(binding.countingLayout);
            if (viewModel.isTimeSelect.value == true) {
                stopTimer();
                binding.layoutTime.visibility = View.GONE;
                binding.layoutSelectCount.visibility = View.VISIBLE;
                binding.btnSetting.text="Hủy"
            } else {
                binding.layoutTime.visibility = View.VISIBLE;
                binding.layoutSelectCount.visibility = View.GONE;
                binding.btnSetting.text="Đặt giờ"
            }
        }

        binding.txt15Min.setOnClickListener {
            var timeset = 15 * 60000L
            MyApplication.startTimer(timeset);
            viewModel.isTimeSelect.postValue(false);
        }

        binding.txt30Min.setOnClickListener {
            var timeset = 30 * 60000L
            MyApplication.startTimer(timeset);
            viewModel.isTimeSelect.postValue(false);
        }

        binding.btnSetting.setOnClickListener {
            viewModel.isTimeSelect.postValue(!viewModel.isTimeSelect.value!!);

        }

        MyApplication.curTimer.observe(this) {
            binding.txtTimeCounting.text = millisecondsToTime(it)
        }

    }

    public fun stopTimer() {
        if (MyApplication.timeIntent != null){
            (MyApplication.mAppContext as Activity).stopService(MyApplication.timeIntent)
            MyApplication.curTimer.postValue(0)
        }
    }

    private fun millisecondsToTime(milliseconds: Long): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        val secondsStr = seconds.toString()
        val secs: String = if (secondsStr.length >= 2) {
            secondsStr.substring(0, 2)
        } else {
            "0$secondsStr"
        }
        var minStr = if (minutes < 10) "0$minutes" else minutes
        var secsStr = if (secs.length < 2) "0$secs" else secs
        return "$minStr:$secsStr"
    }


    lateinit var adapter: TimeListAdapter;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root;
    }

    override fun onResume() {
        binding.viewModel?.currentPos?.postValue(MyApplication.currentMediaPos.value)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




    companion object {
        const val TAG = "BottomSheetDialogFragment"

    }
}