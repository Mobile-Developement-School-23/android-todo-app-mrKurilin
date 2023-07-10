package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.ui.graphics.Color

data class ThemeColors(
    val supportSeparator: Color = Color.Unspecified,
    val supportOverlay: Color = Color.Unspecified,
    val labelPrimary: Color = Color.Unspecified,
    val labelSecondary: Color = Color.Unspecified,
    val labelTertiary: Color = Color.Unspecified,
    val labelDisable: Color = Color.Unspecified,
    val backPrimary: Color = Color.Unspecified,
    val backSecondary: Color = Color.Unspecified,
    val backElevated: Color = Color.Unspecified
) {

    companion object {

        fun darkThemeColors(): ThemeColors {
            return ThemeColors(
                supportSeparator = Color(0x33FFFFFF),
                supportOverlay = Color(0x52000000),
                labelPrimary = Color(0xFFFFFFFF),
                labelSecondary = Color(0x99FFFFFF),
                labelTertiary = Color(0x66FFFFFF),
                labelDisable = Color(0x26FFFFFF),
                backPrimary = Color(0xFF161618),
                backSecondary = Color(0xFF252528),
                backElevated = Color(0xFF3C3C3F),
            )
        }

        fun lightThemeColors(): ThemeColors {
            return ThemeColors(
                supportSeparator = Color(0x33000000),
                supportOverlay = Color(0x0F000000),
                labelPrimary = Color(0xFF000000),
                labelSecondary = Color(0x99000000),
                labelTertiary = Color(0x4D000000),
                labelDisable = Color(0x26000000),
                backPrimary = Color(0xFFF7F6F2),
                backSecondary = Color(0xFFFFFFFF),
                backElevated = Color(0xFFFFFFFF),
            )
        }
    }
}

val Red = Color(0xFFFF3B30)
val Green = Color(0xFF34C759)
val Blue = Color(0xFF007AFF)
val BlueTranslucent = Color(0x4D007AFF)
val Gray = Color(0xFF8E8E93)
val GrayLight = Color(0xFFD1D1D6)
val White = Color(0xFFFFFFFF)

