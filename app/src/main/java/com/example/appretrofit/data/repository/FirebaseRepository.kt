package com.example.appretrofit.data.repository

import com.example.appretrofit.data.models.PlantAnalysisResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface FirebaseRepository {
    suspend fun saveAnalysisResult(result: PlantAnalysisResult)
    suspend fun getAnalysisHistory(deviceId: String): List<PlantAnalysisResult>
}

class FirebaseRepositoryImpl : FirebaseRepository {

    private val db = Firebase.firestore

    // Colección que usas
    private val collection = db.collection("analisis_plantas")

    override suspend fun saveAnalysisResult(result: PlantAnalysisResult) {
        collection.document(result.id).set(
            mapOf(
                "id" to result.id,
                "deviceId" to result.deviceId,
                "name" to result.name,
                "scientificName" to result.scientificName,
                "description" to result.description,
                "confidence" to result.confidence,
                "imageUrl" to result.imageUrl,
                "date" to result.date
            )
        ).await()
    }

    override suspend fun getAnalysisHistory(deviceId: String): List<PlantAnalysisResult> {
        val snapshot = collection
            .whereEqualTo("deviceId", deviceId)   // ← FILTRAR POR DISPOSITIVO!
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            PlantAnalysisResult(
                id = doc.getString("id") ?: return@mapNotNull null,
                deviceId = deviceId,
                name = doc.getString("name") ?: "",
                scientificName = doc.getString("scientificName") ?: "",
                description = doc.getString("description") ?: "",
                confidence = doc.getDouble("confidence") ?: 0.0,
                imageUrl = doc.getString("imageUrl"),
                date = doc.getLong("date") ?: 0L
            )
        }.sortedByDescending { it.date }
    }
}