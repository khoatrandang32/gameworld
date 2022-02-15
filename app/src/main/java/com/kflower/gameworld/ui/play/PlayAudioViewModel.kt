package com.kflower.gameworld.ui.play

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.model.AudioBook
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class PlayAudioViewModel : ViewModel() {
    var item = MutableLiveData<AudioBook>();
    var isPlaying = MutableLiveData(false);
    var isLoading = MutableLiveData(true);
    var duration = MutableLiveData<Long>(0);
    var currentPos = MutableLiveData<Long>(0);
    var currentPart = MutableLiveData("Tập 1");

    fun reset(){
        duration.postValue(0)
        currentPos.postValue(0)
    }

    fun formatSec(s: Long): String {
        var a = s;
        if (a == null) {
            a = 0;
        }
        var res = ""
        if (TimeUnit.MILLISECONDS.toHours(a)==0L) {
            res = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(a) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(a)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(a) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(a))
            );
        }
        else{
            res = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(a),
                TimeUnit.MILLISECONDS.toMinutes(a) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(a)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(a) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(a))
            );
        }

        return res
    }

    fun getProgress(currentPos:Long,duration: Long ):Int{

        if(currentPos == 0L){
            Log.d("KHOA", "getProgress: $currentPos")
            return 0
        }
        return ((currentPos.toFloat() / duration) * 100).roundToInt()
    }
}