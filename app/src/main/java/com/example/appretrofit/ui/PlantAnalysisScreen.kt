package com.example.appretrofit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appretrofit.data.models.PlantAnalysisResult
import com.example.appretrofit.data.repository.FirebaseRepository
import com.example.appretrofit.data.repository.VisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppState(
    val isUploading: Boolean = false,
    val uploadMessage: String = "Selecciona una imagen para analizar.",
    val analysisResult: PlantAnalysisResult? = null,
    val firebaseData: List<PlantAnalysisResult> = emptyList(),
    val error: String? = null
)

class PlantAnalysisViewModel(
    private val visionRepo: VisionRepository,
    private val firebaseRepo: FirebaseRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        fetchFirebaseHistory()
    }

    fun analyzeImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUploading = true, uploadMessage = "Analizando imagen...")
            try {
                val result = visionRepo.uploadImageForAnalysis(imageBytes)
                _state.value = _state.value.copy(
                    isUploading = false,
                    uploadMessage = "Análisis completado.",
                    analysisResult = result,
                    error = null
                )
                // guardar en Firestore
                firebaseRepo.saveAnalysisResult(result)
                fetchFirebaseHistory()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUploading = false,
                    uploadMessage = "Fallo en el análisis.",
                    error = "Error: ${e.localizedMessage}"
                )
            }
        }
    }

    fun fetchFirebaseHistory() {
        viewModelScope.launch {
            try {
                val history = firebaseRepo.getAnalysisHistory()
                _state.value = _state.value.copy(firebaseData = history, error = null)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Error al obtener historial: ${e.localizedMessage}")
            }
        }
    }
}
