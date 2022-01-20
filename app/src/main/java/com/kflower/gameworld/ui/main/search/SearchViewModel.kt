package com.kflower.gameworld.ui.main.search

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.network.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    val searchText = MutableLiveData("")
    val isLoading = MutableLiveData(false)
    val listAudio = MutableLiveData<MutableList<AudioBook>>(arrayListOf())
    var apiService = NetworkProvider.apiService;
    val isError = MutableLiveData(false)

    fun getAudioList(txt: String) {
        isError.postValue(false)
        isLoading.postValue(true)
        apiService.findAudio(txt).enqueue(object : Callback<MutableList<AudioBook>> {
            override fun onResponse(
                call: Call<MutableList<AudioBook>>,
                response: Response<MutableList<AudioBook>>
            ) {
                if (response.isSuccessful ) {
                    isLoading.postValue(false)
                    listAudio.postValue(response.body())
                    Log.d("KHOA", "success: " + response.code())

                } else {
//                    isLoading.postValue(false)
                    Log.d("KHOA", "fail: " + response.code())
                    Handler().postDelayed({
                        isError.postValue(true)
                    }, 1000)
                }
            }

            override fun onFailure(call: Call<MutableList<AudioBook>>, t: Throwable) {
//                isLoading.postValue(false)
                Log.d("KHOA", "res: " + t.message);
                Handler().postDelayed({
                    isError.postValue(true)
                }, 1000)
            }

        })
    }
}