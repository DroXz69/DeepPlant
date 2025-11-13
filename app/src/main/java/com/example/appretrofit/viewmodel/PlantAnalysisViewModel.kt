package com.example.appretrofit.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appretrofit.data.models.PlantAnalysisResult
import com.example.appretrofit.data.models.PlantResponse
import com.example.appretrofit.data.repository.FirebaseRepositoryImpl
import com.example.appretrofit.data.repository.VisionRepository
import com.example.appretrofit.utils.UriUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlantAnalysisViewModel(
    private val repo: VisionRepository = VisionRepository(),
    private val firebaseRepo: FirebaseRepositoryImpl = FirebaseRepositoryImpl()
) : ViewModel() {

    private val _result = MutableStateFlow<PlantResponse?>(null)
    val result: StateFlow<PlantResponse?> = _result

    fun analyze(uri: Uri, resolver: ContentResolver) {
        viewModelScope.launch {
            val multipart = UriUtils.uriToMultipart(resolver, uri)
            val response = repo.analyze(multipart)

            if (response.isSuccessful) {
                val body = response.body()
                _result.value = body

                // Guardar en Firebase
                body?.let { apiResponse ->
                    val firebaseModel = PlantAnalysisResult(
                        id = System.currentTimeMillis().toString(),
                        name = apiResponse.estado,
                        scientificName = "",
                        description = apiResponse.mensaje,
                        confidence = apiResponse.etiquetas.firstOrNull()?.confianza ?: 0.0,
                        imageUrl = null
                    )
                    firebaseRepo.saveAnalysisResult(firebaseModel)
                }
            }
        }
    }
}