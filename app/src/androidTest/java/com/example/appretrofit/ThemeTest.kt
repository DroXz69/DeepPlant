package com.example.appretrofit

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.appretrofit.ui.theme.DeepPlantTheme

class ThemeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun primaryColor_isDefined() {
        composeRule.setContent { TestContent() }
        // Si llega aquí sin excepción asumimos que el tema se compone correctamente
        assert(MaterialTheme.colorScheme.primary.alpha == 1f)
    }

    @Composable
    private fun TestContent() {
        DeepPlantTheme { }
    }
}

