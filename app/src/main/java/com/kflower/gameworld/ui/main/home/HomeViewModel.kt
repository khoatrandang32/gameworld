package com.kflower.gameworld.ui.main.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.dialog.NetworkErrorDialog
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.AudioGroup
import com.kflower.gameworld.network.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    val listAudioGroup = MutableLiveData<MutableList<AudioGroup>>()
    val isLoading = MutableLiveData(false)
    val isError = MutableLiveData(false)
    var apiService = NetworkProvider.apiService;

    fun getAudioList() {
        isLoading.postValue(true)
        isError.postValue(false)
        apiService.getAudioList().enqueue(object : Callback<MutableList<AudioGroup>> {
            override fun onResponse(
                call: Call<MutableList<AudioGroup>>,
                response: Response<MutableList<AudioGroup>>
            ) {
                if (response.isSuccessful and !response.body().isNullOrEmpty()) {
                    isLoading.postValue(false)
                    listAudioGroup.postValue(response.body())
                    Log.d("KHOA", "success: " + response.code())
                } else {
                    Log.d("KHOA", "fail: " + response.code())
                    isError.postValue(true)
                }
            }

            override fun onFailure(call: Call<MutableList<AudioGroup>>, t: Throwable) {
//                NetworkErrorDialog(context = context).show()
                isError.postValue(true)
            }

        })
    }
}