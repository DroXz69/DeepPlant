package com.example.appretrofit.data.repository

import com.example.appretrofit.data.models.PlantAnalysisResult
import com.example.appretrofit.data.network.VisionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface VisionRepository {
    suspend fun uploadImageForAnalysis(imageBytes: ByteArray): PlantAnalysisResult
}

class VisionRepositoryImpl : VisionRepository {
    override suspend fun uploadImageForAnalysis(imageBytes: ByteArray): PlantAnalysisResult = withContext(Dispatchers.IO) {
        val dto = VisionApi.analyzeImageBytes(imageBytes)
        PlantAnalysisResult(
            id = dto.id,
            estado = dto.estado,
            scientificName = dto.scientificName,
            description = dto.details,
            confidence = dto.confidenceScore
        )
    }
}
