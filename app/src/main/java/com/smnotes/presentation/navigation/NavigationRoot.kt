package com.smnotes.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.smnotes.presentation.authScreen.AuthScreen
import com.smnotes.presentation.noteScreen.NoteScreen
import com.smnotes.presentation.notesScreen.NotesScreen
import com.smnotes.presentation.splashScreen.SplashScreen
import com.smnotes.presentation.splashScreen.SplashViewModel
import com.smnotes.presentation.utils.AuthEvent
import com.smnotes.presentation.utils.AuthEventBus
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data object Splash : NavKey

@Serializable
data object Notes : NavKey

@Serializable
data object Auth : NavKey

@Serializable
data class Note(val id: Long, val color: Int) : NavKey


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationRoot(
    onDestinationChanged: (NavKey) -> Unit
) {
    val backStack = rememberNavBackStack(Splash)

    LaunchedEffect(Unit) {
        AuthEventBus.events.collect { event ->
            when (event) {
                is AuthEvent.SessionExpired -> {
                    // Remove any existing Auth entry, then push
                    backStack.removeAll { it is Auth }
                    backStack.add(Auth)
                }
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = rememberTwoPaneSceneStrategy(),
        entryProvider = { key ->
            onDestinationChanged(key)

            when (key) {
                is Splash -> NavEntry(key = key) {
                    val splashViewModel: SplashViewModel = koinViewModel()
                    SplashScreen(
                        isReady = splashViewModel.isReady,
                        onFinished = {
                            backStack.add(Notes)
                            backStack.remove(Splash)
                        }
                    )
                }

                is Notes -> NavEntry(key = key, metadata = TwoPaneScene.twoPane()) {
                    NotesScreen(
                        onOpenNote = { id, color -> backStack.add(Note(id, color)) },
                        onNavigateToAuth = { backStack.add(Auth) }
                    )
                }

                is Auth -> NavEntry(key = key) {
                    AuthScreen(
                        onSuccess = { backStack.removeLastOrNull() },
                        onContinueWithoutAccount = { backStack.removeLastOrNull() }
                    )
                }

                is Note -> NavEntry(key = key, metadata = TwoPaneScene.twoPane()) {
                    NoteScreen(
                        viewModel = koinViewModel { parametersOf(key.id, key.color) },
                        onNavigateUp = { backStack.removeLastOrNull() }
                    )
                }

                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}
