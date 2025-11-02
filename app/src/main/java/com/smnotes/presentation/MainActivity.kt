package com.smnotes.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowInsetsControllerCompat
import com.smnotes.presentation.navigation.NavigationRoot
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.smnotes.presentation.navigation.Splash
import com.smnotes.presentation.theme.LocalThemeState
import com.smnotes.presentation.theme.SMNotesTheme
import com.smnotes.presentation.theme.ThemeState
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val application: NoteApp by inject()


    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )

        setContent {
            // Keep ThemeState stable - only recreate if application changes
            // This avoids unnecessary recompositions while maintaining reactivity
            val themeState = remember(application) {
                ThemeState(
                    isDark = application.isDark,
                    toggleTheme = { application.toggleLightTheme() }
                )
            }
            
            val isDark by themeState.isDark
            
            // Provide theme state via CompositionLocal
            // ThemeState is stable, so only composables reading isDark recompose
            CompositionLocalProvider(LocalThemeState provides themeState) {
                SMNotesTheme(isDark) {
                    var isSplashScreen by remember { mutableStateOf(false) }

                    NavigationRoot(
                        onDestinationChanged = { key ->
                            isSplashScreen = key is Splash
                        }
                    )

                    // Update status bar text color based on current destination
                    SetSystemBarColors(
                        isDark = isDark,
                        isSplashScreen = isSplashScreen
                    )
                }
            }
        }
    }

    @Composable
    private fun SetSystemBarColors(isDark: Boolean, isSplashScreen: Boolean = false){
        SideEffect {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            // If splash screen, use black text; otherwise use theme-based text color
            controller.isAppearanceLightStatusBars = if(isSplashScreen) false else !isDark
            controller.isAppearanceLightNavigationBars =  if (isSplashScreen) true else !isDark
        }
    }


}
