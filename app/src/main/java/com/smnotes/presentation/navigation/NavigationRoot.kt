package com.smnotes.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.smnotes.presentation.notesScreen.NotesScreen
import com.smnotes.presentation.noteScreen.NoteScreen
import com.smnotes.presentation.splashScreen.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object Splash : NavKey

@Serializable
data object Notes : NavKey

@Serializable
data class Note(val id: Long, val color: Int) : NavKey


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationRoot(
    onDestinationChanged: (NavKey) -> Unit
) {
    //Another approach to managing your back stack is to store it in a ViewModel
    val backStack = rememberNavBackStack(Splash)

    NavDisplay(
        backStack = backStack,

        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(), //Allows ViewModels to be scoped to entries in the back stack.

        ),
        entryProvider = { key ->
            // Notify host about destination change
            onDestinationChanged(key)

            when (key) {
                is Splash -> NavEntry(
                    key = key,
                ) {
                    SplashScreen(
                        onFinished = {
                            backStack.add(Notes)
                            backStack.remove(Splash)
                        }
                    )
                }

                is Notes -> NavEntry(
                    key = key,
                ) {
                    NotesScreen(
                        onOpenNote = { id, color -> backStack.add(Note(id, color)) }
                    )
                }

                is Note -> NavEntry(
                    key = key,
                ) {
                    NoteScreen(
                        noteId = key.id,
                        noteColor = key.color,
                        onNavigateUp = { backStack.removeLastOrNull() }
                    )
                }

                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}



