package com.smnotes.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowInsetsControllerCompat
// Navigation Compose (using standard API - Navigation 3 Compose integration pending)
// Using simple key-based navigator host
import com.smnotes.presentation.navigation.NavigationRoot
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.smnotes.presentation.navigation.Splash
import com.smnotes.presentation.theme.SMNotesTheme
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
            SMNotesTheme(application.isDark) {
                var isSplashScreen by remember { mutableStateOf(false) }

                NavigationRoot(
                    onDestinationChanged = { key ->
                        isSplashScreen = key is Splash
                    }
                )

                // Update status bar text color based on current destination
                SetSystemBarColors(
                    isDark = application.isDark,
                    isSplashScreen = isSplashScreen
                )
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
