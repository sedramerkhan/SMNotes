package com.smnotes.presentation.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal for accessing theme state throughout the app.
 * Provides read and write access to the current theme state.
 * 
 * Performance: Holds State<Boolean> directly to avoid unnecessary object recreation
 * while maintaining reactivity. Only composables reading isDark will recompose.
 */
data class ThemeState(
    val isDark: State<Boolean>,
    val toggleTheme: () -> Unit
)

val LocalThemeState = compositionLocalOf<ThemeState> {
    error("No ThemeState provided. Make sure to provide ThemeState via CompositionLocalProvider.")
}

