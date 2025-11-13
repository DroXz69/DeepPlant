package com.example.appretrofit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006837),
    secondary = Color(0xFF5E5E5E),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00A86B),
    secondary = Color(0xFFCCCCCC),
)
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val Shapes = Shapes()

    MaterialTheme(

        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}