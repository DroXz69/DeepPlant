package com.example.appretrofit.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageSaver {

    fun saveImage(context: Context, uri: Uri): String? {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null

            val folder = File(context.getExternalFilesDir(null), "plant_images")
            if (!folder.exists()) folder.mkdirs()

            val filename = "plant_${System.currentTimeMillis()}.jpg"
            val file = File(folder, filename)

            val output = FileOutputStream(file)
            input.copyTo(output)

            input.close()
            output.close()

            file.absolutePath

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
