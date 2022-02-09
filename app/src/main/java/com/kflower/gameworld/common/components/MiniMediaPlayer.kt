package com.kflower.gameworld.common.components

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.imageview.ShapeableImageView
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.mMediaItem
import com.kflower.gameworld.MyApplication.Companion.mediaPlayer
import com.kflower.gameworld.R
import com.kflower.gameworld.common.PlayAudioManager
import com.kflower.gameworld.common.lifecycleOwner
import com.kflower.gameworld.interfaces.isMediaPlayChanged
import glimpse.glide.GlimpseTransformation
import kotlin.math.roundToInt

class MiniMediaPlayer : LinearLayout {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.d(TAG, "onLayout: ")
    }

    lateinit var seekBar: SeekBar;
    lateinit var container: LinearLayout;
    lateinit var txtAudioBookName: TextView;
    lateinit var txtAudioAuthor: TextView;
    lateinit var imgAudio: ShapeableImageView;
    lateinit var imgPlayAndPause: ImageView;
    lateinit var imgPre: ImageView;
    lateinit var imgNext: ImageView;
    lateinit var progressBar: ProgressBar;
    var listener: View.OnClickListener?=null

    private lateinit var durationHandler: Handler
    var duration:Long= 0;

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    override fun onAttachedToWindow() {

        super.onAttachedToWindow()
        duration= mediaPlayer.duration
        Log.d(TAG, "onAttachedToWindow: "+ getProgress(mediaPlayer.currentPosition,duration))
        seekBar.progress = getProgress(mediaPlayer.currentPosition,duration)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private fun initView(attrs: AttributeSet?) {
        durationHandler = Handler(Looper.getMainLooper());

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.mini_player_layout, null
        )
        view.layoutParams = layoutParams
        this.addView(view)

        seekBar = findViewById(R.id.seekBar);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progressBar);
        imgPre = findViewById(R.id.imgPre);
        imgNext = findViewById(R.id.imgNext);
        imgAudio = findViewById(R.id.imgAudio);
        txtAudioBookName = findViewById(R.id.txtAudioBookName);
        txtAudioAuthor = findViewById(R.id.txtAudioAuthor);
        imgPlayAndPause = findViewById(R.id.imgPlayAndPause);
        seekBar.isEnabled = false;
        seekBar.setPadding(0, 0, 0, 0)
        Glide.with(this)
            .load(PlayAudioManager.playingAudio?.thumbnailUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(GlimpseTransformation())
            .into(imgAudio)
        container.visibility= if (PlayAudioManager.playingAudio!=null)  View.VISIBLE else GONE;
        txtAudioBookName.text = PlayAudioManager.playingAudio?.title+" - Tap "+ (mediaPlayer.currentMediaItemIndex+1)
        txtAudioAuthor.text = PlayAudioManager.playingAudio?.author
        initViewData();
        txtAudioBookName.isSelected=true;

        duration= mediaPlayer.duration

        MyApplication.mAppContext?.lifecycleOwner()?.let {
            MyApplication.mIsPlaying.observe(it,{ isPlaying ->
                imgPlayAndPause.setImageDrawable(
                    context.getDrawable(
                        if (isPlaying) R.drawable.ic_pause
                        else R.drawable.ic_play
                    )
                )
                if(isPlaying){
                    (context as Activity).runOnUiThread(updateSeekBarTime)
                }
                else{
                    durationHandler.removeCallbacks(updateSeekBarTime);
                }
            })
            //
            MyApplication.mPlaybackState.observe(it,{
                Glide.with(context)
                    .load(PlayAudioManager.playingAudio?.thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(GlimpseTransformation())
                    .into(imgAudio)
                container.visibility= if (PlayAudioManager.playingAudio!=null)  View.VISIBLE else GONE;
                txtAudioBookName.text = PlayAudioManager.playingAudio?.title+" - Tap "+ (mediaPlayer.currentMediaItemIndex+1)
                txtAudioAuthor.text = PlayAudioManager.playingAudio?.author
                duration= mediaPlayer.duration
                progressBar.visibility= GONE
                imgPlayAndPause.visibility= VISIBLE
            })

            mMediaItem.observe(it,{
                progressBar.visibility= GONE
                imgPlayAndPause.visibility= VISIBLE
//                seekBar.progress= 0;
                seekBar.progress = getProgress(mediaPlayer.currentPosition,duration)
                txtAudioBookName.text = PlayAudioManager.playingAudio?.title+" - Tap "+ (mediaPlayer.currentMediaItemIndex+1)
                txtAudioAuthor.text = PlayAudioManager.playingAudio?.author
            })
        }

        imgPlayAndPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                imgPlayAndPause.setImageDrawable(context.getDrawable(R.drawable.ic_play_solid))
            } else {
                mediaPlayer.play()
                imgPlayAndPause.setImageDrawable(context.getDrawable(R.drawable.ic_pause_solid))
            }
        }
        imgNext.setOnClickListener {
            mediaPlayer.seekToNextMediaItem()
            if(mediaPlayer.currentMediaItemIndex!=(PlayAudioManager.playingAudio?.episodes?.size!! -1)){
                progressBar.visibility= VISIBLE
                imgPlayAndPause.visibility= GONE
            }

        }
        imgPre.setOnClickListener {
            mediaPlayer.seekToPreviousMediaItem()
            if(mediaPlayer.currentMediaItemIndex!=0){
                progressBar.visibility= VISIBLE
                imgPlayAndPause.visibility= GONE
            }
        }
        container.setOnClickListener {
            listener?.onClick(it)
        }
    }

    private val updateSeekBarTime: Runnable = object : Runnable {
        override fun run() {
            seekBar.progress = getProgress(mediaPlayer.currentPosition,duration)
            durationHandler.postDelayed(this, 1000)
        }
    }

    fun getProgress(currentPos:Long,duration: Long ):Int{

        if(currentPos == 0L){
            return 0
        }
        return ((currentPos.toFloat() / duration) * 100).roundToInt()
    }

    fun setOnClick(listener: OnClickListener){
        this.listener= listener
    }

    private fun initViewData() {
        imgPlayAndPause.setImageDrawable(
            context.getDrawable(
                if (mediaPlayer.isPlaying) R.drawable.ic_pause
                else R.drawable.ic_play
            )
        )
    }



}