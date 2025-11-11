package com.example.appretrofit.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.client.request.forms.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.appretrofit.data.models.ApiVisionResponse

object VisionApi {
    private const val BASE_URL = "https://apivisionplant.inacode.cl"
    private const val ANALYZE_PATH = "/api/analyze"
    val client: HttpClient by lazy {
        HttpClient(io.ktor.client.engine.okhttp.OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }

    suspend fun analyzeImageBytes(imageBytes: ByteArray, filename: String = "image.jpg"): ApiVisionResponse {
        val response = client.submitFormWithBinaryData(
            url = "$BASE_URL$ANALYZE_PATH",
            formData = formData {
                append(
                    key = "image",
                    value = imageBytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentDisposition, "form-data; name=\"image\"; filename=\"$filename\"")
                        append(HttpHeaders.ContentType, "image/jpeg")
                    }
                )
            }
        )

        // Si la API responde JSON, convertimos a ApiVisionResponse
        return response.body()
    }
}