package com.example.appretrofit.utils
import android.content.Context
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

object UriUtils {
    fun readBytesFromUri(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            readBytes(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun readBytes(input: InputStream): ByteArray {
        val buffer = ByteArrayOutputStream()
        val data = ByteArray(16384)
        var nRead: Int
        while (true) {
            nRead = input.read(data, 0, data.size)
            if (nRead == -1) break
            buffer.write(data, 0, nRead)
        }
        return buffer.toByteArray()
    }
}
