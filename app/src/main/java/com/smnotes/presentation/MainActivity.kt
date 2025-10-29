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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
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

                // Navigation graph
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        com.smnotes.presentation.splashScreen.SplashScreen(
                            onFinished = {
                                navController.navigate("notes") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("notes") {
                        com.smnotes.presentation.notesScreen.NotesScreen(
                            onOpenNote = { id, color ->
                                navController.navigate("note/$id/$color")
                            }
                        )
                    }
                    composable(
                        route = "note/{id}/{color}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.LongType },
                            navArgument("color") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getLong("id") ?: -1L
                        val color = backStackEntry.arguments?.getInt("color") ?: -1
                        com.smnotes.presentation.noteScreen.NoteScreen(
                            noteId = id,
                            noteColor = color,
                            onNavigateUp = { navController.popBackStack() }
                        )
                    }
                }

                // Update status bar text color based on current destination
                val isSplashScreen = currentRoute?.contains("splash") == true
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
