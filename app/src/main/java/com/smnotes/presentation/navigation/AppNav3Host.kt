package com.smnotes.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material.Text
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.smnotes.presentation.notesScreen.NotesScreen
import com.smnotes.presentation.noteScreen.NoteScreen
import com.smnotes.presentation.splashScreen.SplashScreen

sealed interface ScreenKey {
    data object Splash : ScreenKey
    data object Notes : ScreenKey
    data class Note(val id: Long, val color: Int) : ScreenKey
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav3Host(
    onDestinationChanged: (ScreenKey) -> Unit
) {
    val backStack = remember { mutableStateListOf<ScreenKey>(ScreenKey.Splash) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() }
    ) { key ->
        // Notify host about destination change
        onDestinationChanged(key)
        when (key) {
            is ScreenKey.Splash -> NavEntry(key) {
                SplashScreen(
                    onFinished = {
                        backStack.add(ScreenKey.Notes)
                        backStack.remove(ScreenKey.Splash)
                    }
                )
            }

            is ScreenKey.Notes -> NavEntry(key) {
                NotesScreen(
                    onOpenNote = { id, color -> backStack.add(ScreenKey.Note(id, color)) }
                )
            }

            is ScreenKey.Note -> NavEntry(key) {
                NoteScreen(
                    noteId = key.id,
                    noteColor = key.color,
                    onNavigateUp = { backStack.removeLastOrNull() }
                )
            }
        }
    }
}



