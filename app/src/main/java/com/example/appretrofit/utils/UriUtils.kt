package com.example.appretrofit.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object UriUtils {

    fun uriToMultipart(resolver: ContentResolver, uri: Uri): MultipartBody.Part {
        val inputStream = resolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se pudo abrir la imagen")

        val bytes = inputStream.readBytes()
        inputStream.close()

        if (bytes.isEmpty()) {
            throw IllegalArgumentException("La imagen está vacía")
        }

        val mediaType = "image/jpeg".toMediaType()
        val requestBody = bytes.toRequestBody(mediaType)

        return MultipartBody.Part.createFormData(
            "image",
            "photo.jpg",
            requestBody
        )
    }
}