package com.example.appretrofit.data.repository

import com.example.appretrofit.data.network.RetrofitClient
import okhttp3.MultipartBody

class VisionRepository {

    suspend fun analyze(image: MultipartBody.Part) =
        RetrofitClient.api.analyzeImage(image)
}
