package com.kflower.gameworld.ui.audioDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.network.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioDetailViewModel:ViewModel() {
    var item= MutableLiveData<AudioBook>();
    var apiService = NetworkProvider.apiService;

    fun getAudioDetail(id:String) {
        apiService.getAudioDetail(id).enqueue(object : Callback<AudioBook> {
            override fun onResponse(
                call: Call<AudioBook>,
                response: Response<AudioBook>
            ) {
                if (response.isSuccessful) {
                    item.postValue(response.body())
                    Log.d("KHOA", "success: " + response.code())

                } else {
                    Log.d("KHOA", "fail: " + response.code())
                }
            }

            override fun onFailure(call: Call<AudioBook>, t: Throwable) {
                Log.d("KHOA", "res: " + t.message);
            }

        })
    }
}