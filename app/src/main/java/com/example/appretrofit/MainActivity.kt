package com.example.appretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.appretrofit.ui.PlantAnalysisScreen
import com.example.appretrofit.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PlantAnalysisScreen()
            }
        }
        screenSplash.setKeepOnScreenCondition { false }
    }
}
