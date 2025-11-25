package com.example.appretrofit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Semantic / extended colors no cubiertos directamente por el esquema Material3
data class ExtendedColors(
    val success: Color,
    val warning: Color,
    val danger: Color,
    val backgroundElevated: Color,
    val surfaceSecondary: Color
)

// Reemplazo staticCompositionLocalOf por compositionLocalOf y función de acceso sencilla
private val LocalExtendedColors = compositionLocalOf {
    ExtendedColors(
        success = Color(0xFF4CAF50),
        warning = Color(0xFFFFC107),
        danger = Color(0xFFE53935),
        backgroundElevated = Color(0xFFFFFFFF),
        surfaceSecondary = Color(0xFFF1F5F2)
    )
}

@Composable
fun deepPlantExtendedColors(): ExtendedColors = LocalExtendedColors.current

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = GreenOnPrimaryContainer,
    secondary = GreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = GreenSecondaryContainer,
    onSecondaryContainer = GreenOnSecondaryContainer,
    tertiary = AccentTertiary,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = DarkText,
    surface = LightSurface,
    onSurface = DarkText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = OutlineColor
)

private val DarkColors = darkColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = Color.Black,
    primaryContainer = GreenPrimaryDarkContainer,
    onPrimaryContainer = Color.Black,
    secondary = GreenSecondaryDark,
    onSecondary = Color.Black,
    secondaryContainer = GreenSecondaryDarkContainer,
    onSecondaryContainer = Color.Black,
    tertiary = AccentTertiaryDark,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = Color(0xFFE0E0E0),
    surface = DarkSurface,
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = OutlineDarkColor
)

@Composable
fun DeepPlantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val extended = if (darkTheme) {
        ExtendedColors(
            success = Color(0xFF66BB6A),
            warning = Color(0xFFFFD54F),
            danger = Color(0xFFEF5350),
            backgroundElevated = DarkSurface,
            surfaceSecondary = DarkSurfaceVariant
        )
    } else {
        ExtendedColors(
            success = Color(0xFF2E7D32),
            warning = Color(0xFFFFB300),
            danger = Color(0xFFD32F2F),
            backgroundElevated = LightSurface,
            surfaceSecondary = LightSurfaceVariant
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? Activity)?.window
        window?.let {
            // Edge-to-edge y color de barras transparentes
            WindowCompat.setDecorFitsSystemWindows(it, false)
            // Podríamos ajustar status bar icon tint según tema (simple: claro u oscuro)
        }
    }

    androidx.compose.runtime.CompositionLocalProvider(LocalExtendedColors provides extended) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

// Accesos de conveniencia
val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current
