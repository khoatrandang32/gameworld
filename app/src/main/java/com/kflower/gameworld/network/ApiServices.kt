package com.kflower.gameworld.network

import com.kflower.gameworld.model.AudioBook
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {
    @POST("/audios/getAudio")
    fun getAudioList(): Call<MutableList<AudioBook>>

    @GET("/audios/getAudio/{id}")
    fun getAudioDetail(@Path("id") customerId: String): Call<AudioBook>

    @FormUrlEncoded
    @POST("/audios/find")
    fun findAudio(
        @Field("title") title: String
    ): Call<MutableList<AudioBook>>

}