package com.smnotes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
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

        setContent {
            SMNotesTheme(application.isDark) {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
