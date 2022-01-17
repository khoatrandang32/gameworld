package com.kflower.gameworld.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProvider {
    private const val BASE_URL = "http://192.168.1.102:3001"


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }

    val apiService: ApiServices = getRetrofit().create(ApiServices::class.java)

}