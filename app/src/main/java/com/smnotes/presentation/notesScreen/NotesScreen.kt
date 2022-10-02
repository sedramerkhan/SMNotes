package com.smnotes.presentation.notesScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.smnotes.presentation.destinations.NoteScreenDestination
import com.smnotes.presentation.notesScreen.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
@Destination
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator,
    viewModel: NotesViewModel = hiltViewModel()
) = viewModel.run {
    val state = state.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val clipboardManager = LocalClipboardManager.current

    val drawerState = scaffoldState.drawerState

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopAppBar(title = selectedItemDrawer.value,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.animateTo(DrawerValue.Open, tween(500))
                            }
                        },
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(NotesEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(NoteScreenDestination(-1, -1))
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Add note",
                    tint = Color.Black
                )
            }
        },
        drawerContent = {
            MainDrawer(selected = selectedItemDrawer, onItemSelected = {
                if (selectedItemDrawer != it) {
                    selectedItemDrawer = it
                    onEvent(NotesEvent.GetNotes)
                }
                scope.launch {
                    drawerState.animateTo(DrawerValue.Closed, tween(800))
                }
            }, isDark = application.isDark, toggleLightTheme = { application.toggleLightTheme() })
        }
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier.fillMaxWidth(),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes, key = { it.id }) { note ->
                    val dismissState = DismissState_Start_End(
                        DismissedToStart = {
                            onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        },
                        DismissedToEnd = {
                            clipboardManager.setText(AnnotatedString(note.title + "\n" + note.content))
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Copied",
                                )
                            }
                        })

                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(NoteScreenDestination(note.id, note.color))
                            }
                            .animateItemPlacement(
                                animationSpec = tween(500)
                            ),
                        dismissInfo = DismissInfo(
                            dismissState = dismissState,
                            background = {
                                SwipeBackground()
                            }
                        ),
                        onImportantClick = {
                            onEvent(NotesEvent.ImportantNote(note))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }


    }
}