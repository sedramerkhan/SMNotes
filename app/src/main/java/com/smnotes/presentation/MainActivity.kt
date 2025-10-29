package com.smnotes.presentation

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.smnotes.presentation.theme.SMNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var application: NoteApp


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
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                Log.i("currentRoute", currentRoute ?: "null")
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController
                )

                // Update status bar text color based on current destination
                val isSplashScreen = currentRoute?.contains("splash_screen") == true
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
