package com.example.appretrofit.data.models

// RESPUESTA DE LA API
data class PlantResponse(
    val mensaje: String,
    val estado: String,
    val etiquetas: List<Etiqueta>
)

data class Etiqueta(
    val descripcion: String,
    val confianza: Double
)

// MODELO PARA GUARDAR EN FIREBASE
data class PlantAnalysisResult(
    val id: String = "",
    val deviceId: String = "",
    val name: String = "",
    val scientificName: String = "",
    val description: String = "",
    val confidence: Double = 0.0,
    val imageUrl: String? = null,
    val date: Long = System.currentTimeMillis()
)