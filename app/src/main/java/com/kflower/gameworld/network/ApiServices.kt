package com.kflower.gameworld.network

import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.AudioGroup
import com.kflower.gameworld.model.Category
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {
    @GET("/audios/getHomeCate")
    fun getAudioList(): Call<MutableList<AudioGroup>>

    @GET("/audios/getAudio/{id}")
    fun getAudioDetail(@Path("id") customerId: String): Call<AudioBook>

    @FormUrlEncoded
    @POST("/audios/find")
    fun findAudio(
        @Field("title") title: String
    ): Call<MutableList<AudioBook>>

    @GET("/categories/getAll")
    fun getAllCategories(): Call<MutableList<Category>>

    @GET("/audios/getByCategory/{categoryId}")
    fun getListAudioByCategory(
        @Path("categoryId") categoryId: String
    ): Call<MutableList<AudioBook>>

}