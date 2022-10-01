package com.smnotes.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@SuppressLint("ConflictingOnColor")
private val LightThemeColors = lightColors(
    primary = Pink50,
    primaryVariant = Purple20,
    onPrimary = Black2,
    secondary = Pink20,
    secondaryVariant = Color.White,
    onSecondary = Color.Black,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Black2,
)

private val DarkThemeColors = darkColors(
    primary = Pink50,
    primaryVariant = Color.White,
    onPrimary = Color.White,
    secondary = Pink20,
    secondaryVariant = Color.White,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    onSurface = Color.White,
)



@Composable
fun SMNotesTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {

    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
       typography = LoraTypography,
        shapes = Shapes
    ) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(Color.Transparent)
            systemUiController.statusBarDarkContentEnabled = !darkTheme
            systemUiController.navigationBarDarkContentEnabled = !darkTheme
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.surface).padding(bottom = 45.dp)
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                content()
            }
        }
    }
}

