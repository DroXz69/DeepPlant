package com.example.appretrofit.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://apivisionplant.inacode.cl/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: VisionApi = retrofit.create(VisionApi::class.java)
}