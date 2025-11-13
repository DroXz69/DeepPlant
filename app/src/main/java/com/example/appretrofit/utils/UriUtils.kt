package com.example.appretrofit.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

object UriUtils {

    fun uriToMultipart(resolver: ContentResolver, uri: Uri): MultipartBody.Part {
        val bytes = resolver.openInputStream(uri)?.readBytes()
            ?: ByteArray(0)

        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), bytes)

        return MultipartBody.Part.createFormData(
            "image",
            "photo.jpg",
            requestBody
        )
    }
}