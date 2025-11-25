package com.example.appretrofit

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.appretrofit.ui.PlantAnalysisScreen
import com.example.appretrofit.ui.theme.DeepPlantTheme

class MainActivity : ComponentActivity() {
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar splash screen (compat + Android 12+)
        val splash = installSplashScreen()
        startTime = SystemClock.uptimeMillis()

        // Mantener el splash hasta cumplir duración mínima (1200ms)
        splash.setKeepOnScreenCondition {
            val elapsed = SystemClock.uptimeMillis() - startTime
            elapsed < 1200
        }

        super.onCreate(savedInstanceState)
        setContent {
            DeepPlantTheme(dynamicColor = true) {
                PlantAnalysisScreen()
            }
        }
    }
}
