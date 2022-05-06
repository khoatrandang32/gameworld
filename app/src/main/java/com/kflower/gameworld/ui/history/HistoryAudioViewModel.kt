package com.kflower.gameworld.ui.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.model.AudioBook

class HistoryAudioViewModel:ViewModel() {
    val listAudio = MutableLiveData<MutableList<AudioBook>>(arrayListOf())

    fun setUpData(){
        var listData = MyApplication.audioTable.getAll()

        Log.d(TAG, "setUpData: "+listData.size)
        listAudio.postValue(listData)
    }
}