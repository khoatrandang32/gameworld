package com.kflower.gameworld.ui.main.categories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.Category
import com.kflower.gameworld.network.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesViewModel : ViewModel() {
    val listCategories = MutableLiveData<MutableList<Category>>(arrayListOf())
    val isLoading = MutableLiveData(false)
    var apiService = NetworkProvider.apiService;

    fun getCategories() {
        isLoading.postValue(true)
        apiService.getAllCategories().enqueue(object : Callback<MutableList<Category>> {
            override fun onResponse(
                call: Call<MutableList<Category>>,
                response: Response<MutableList<Category>>
            ) {
                if (response.isSuccessful ) {
                    listCategories.postValue(response.body())
                    Log.d("KHOA", "success: " + response.code())
                    isLoading.postValue(false)

                } else {
                    Log.d("KHOA", "fail: " + response.code())
//                    isLoading.postValue(false)
                }
            }

            override fun onFailure(call: Call<MutableList<Category>>, t: Throwable) {
                Log.d("KHOA", "res: " + t.message);
//                isLoading.postValue(false)
            }

        })
    }
}