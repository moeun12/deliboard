package com.example.deliboard

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("room/login")
    fun sendStoreInfo(@Body requestBody: Map<String, String>): Call<Void>
}