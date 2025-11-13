package com.example.appretrofit.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appretrofit.data.models.PlantAnalysisResult
import com.example.appretrofit.data.models.PlantResponse
import com.example.appretrofit.data.repository.FirebaseRepositoryImpl
import com.example.appretrofit.data.repository.VisionRepository
import com.example.appretrofit.utils.ImageSaver
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

    private val _history = MutableStateFlow<List<PlantAnalysisResult>>(emptyList())
    val history: StateFlow<List<PlantAnalysisResult>> = _history

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Guardamos deviceId aquí
    private var deviceId: String? = null

    fun initDevice(context: Context) {
        deviceId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        loadHistory()
    }

    fun analyze(uri: Uri, resolver: ContentResolver, context: Context) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val multipart = UriUtils.uriToMultipart(resolver, uri)
                val response = repo.analyze(multipart)

                if (response.isSuccessful) {
                    val body = response.body()
                    _result.value = body

                    // ------------------------------------------------------------------
                    // GUARDAR IMAGEN LOCAL
                    val savedImagePath = ImageSaver.saveImage(context, uri)
                    // ------------------------------------------------------------------

                    val firebaseModel = PlantAnalysisResult(
                        id = System.currentTimeMillis().toString(),
                        deviceId = deviceId ?: "",
                        name = body?.estado ?: "Desconocido",
                        scientificName = "",
                        description = body?.mensaje ?: "",
                        confidence = body?.etiquetas?.firstOrNull()?.confianza ?: 0.0,
                        imageUrl = savedImagePath,
                        date = System.currentTimeMillis()
                    )

                    firebaseRepo.saveAnalysisResult(firebaseModel)
                    loadHistory()
                }

                _loading.value = false

            } catch (e: Exception) {
                e.printStackTrace()
                _loading.value = false
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            try {
                deviceId?.let {
                    _history.value = firebaseRepo.getAnalysisHistory(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}