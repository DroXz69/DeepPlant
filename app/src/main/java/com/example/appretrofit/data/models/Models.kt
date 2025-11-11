@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)

package com.example.appretrofit.data.models

import kotlinx.serialization.Serializable

data class PlantAnalysisResult(
    val id: String,
    val name: String,
    val scientificName: String,
    val description: String,
    val confidence: Double,
    val imageUrl: String? = null
)

@Serializable
data class ApiVisionResponse(
    val id: String,
    val name: String,
    val scientificName: String,
    val description: String,
    val confidence: Double,
    val imageUrl: String? = null
)
