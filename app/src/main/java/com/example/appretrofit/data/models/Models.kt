@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)

package com.example.appretrofit.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PlantAnalysisResult(
    val descripcion: String,
    val confianza: Double
)
@Serializable
data class ApiVisionResponse(
    val mensaje: String,
    val estado: String,
    val etiquetas: List<PlantAnalysisResult>
)