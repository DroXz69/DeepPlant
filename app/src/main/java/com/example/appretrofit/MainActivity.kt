package com.example.appretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.appretrofit.repository.FirebaseRepository
import com.example.appretrofit.repository.VisionRepository
import com.example.appretrofit.ui.PlantAnalysisScreen
import com.example.appretrofit.viewmodel.PlantAnalysisViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.appretrofit.ui.theme.AppRetrofitTheme // opcional

class MainActivity : ComponentActivity() {
    private val visionRepo: VisionRepository by lazy { VisionRepositoryImpl() }
    private val firebaseRepo: FirebaseRepository by lazy { FirebaseRepositoryImpl() }
    private val viewModel by lazy { PlantAnalysisViewModel(visionRepo, firebaseRepo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa Firebase (asegúrate de tener google-services.json y configuración)
        FirebaseApp.initializeApp(this)

        setContent {
            // YourAppTheme { // si tienes un theme
            Surface(color = MaterialTheme.colors.background) {
                PlantAnalysisScreen(viewModel)
            }
            // }
        }
    }
}
