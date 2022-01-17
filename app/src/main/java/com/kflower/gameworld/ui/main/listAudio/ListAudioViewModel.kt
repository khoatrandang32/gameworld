package com.kflower.gameworld.ui.main.listAudio

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.Category
import com.kflower.gameworld.network.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAudioViewModel:ViewModel() {
    val curCategory = MutableLiveData<Category>()
    val listAudio = MutableLiveData<MutableList<AudioBook>>(arrayListOf())
    var apiService = NetworkProvider.apiService;

    fun getAudioListByCategory(id: String) {
        apiService.getListAudioByCategory(id).enqueue(object : Callback<MutableList<AudioBook>> {
            override fun onResponse(
                call: Call<MutableList<AudioBook>>,
                response: Response<MutableList<AudioBook>>
            ) {
                if (response.isSuccessful ) {
                    listAudio.postValue(response.body())
                    Log.d("KHOA", "success: " + response.code())

                } else {
                    Log.d("KHOA", "fail: " + response.code())
                }
            }

            override fun onFailure(call: Call<MutableList<AudioBook>>, t: Throwable) {
                Log.d("KHOA", "res: " + t.message);
            }

        })
    }
}