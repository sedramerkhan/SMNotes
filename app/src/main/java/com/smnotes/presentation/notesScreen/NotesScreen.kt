package com.smnotes.presentation.notesScreen

import androidx.compose.animation.*
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
@Destination(start = true)
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator,
    viewModel: NotesViewModel = hiltViewModel()
) = viewModel.run {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val clipboardManager = LocalClipboardManager.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopAppBar(title = "Notes", navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
//                           drawerState.animateTo(DrawerValue.Open,tween(500))
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }
            }) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(NoteScreenDestination(-1, -1))
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        drawerContent = {
            Text("hello")
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
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes, key = { it.id }) { note ->
                    val dismissState = DismissState_Start_End(
                        DismissedToStart = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
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
                            viewModel.onEvent(NotesEvent.ImportantNote(note))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}