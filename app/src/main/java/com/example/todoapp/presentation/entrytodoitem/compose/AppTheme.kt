package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeColors = staticCompositionLocalOf {
    ThemeColors()
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) {
        ThemeColors.darkThemeColors()
    } else {
        ThemeColors.lightThemeColors()
    }

    CompositionLocalProvider(LocalThemeColors provides colors) {
        MaterialTheme(
            colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
            typography = Typography(),
            content = content
        )
    }
}

object GetThemeColors {
    val colors: ThemeColors
        @Composable
        get() = LocalThemeColors.current
}
