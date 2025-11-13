package com.example.appretrofit.data.repository

import com.example.appretrofit.data.models.PlantAnalysisResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface FirebaseRepository {
    suspend fun saveAnalysisResult(result: PlantAnalysisResult)
    suspend fun getAnalysisHistory(): List<PlantAnalysisResult>
}

class FirebaseRepositoryImpl : FirebaseRepository {
    private val db = Firebase.firestore
    private val collection = db.collection("analysis_history")

    override suspend fun saveAnalysisResult(result: PlantAnalysisResult) {
        val map = hashMapOf(
            "id" to result.id,
            "name" to result.name,
            "scientificName" to result.scientificName,
            "description" to result.description,
            "confidence" to result.confidence,
            "imageUrl" to result.imageUrl
        )
        collection.document(result.id).set(map).await()
    }

    override suspend fun getAnalysisHistory(): List<PlantAnalysisResult> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            val id = doc.getString("id") ?: return@mapNotNull null
            val name = doc.getString("name") ?: "Desconocida"
            val scientificName = doc.getString("scientificName") ?: ""
            val description = doc.getString("description") ?: ""
            val confidence = doc.getDouble("confidence") ?: 0.0
            val imageUrl = doc.getString("imageUrl")
            PlantAnalysisResult(
                id = id,
                name = name,
                scientificName = scientificName,
                description = description,
                confidence = confidence,
                imageUrl = imageUrl
            )
        }.sortedByDescending { it.id }
    }
}