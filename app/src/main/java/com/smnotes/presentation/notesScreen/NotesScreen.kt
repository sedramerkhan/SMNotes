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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.smnotes.presentation.notesScreen.components.*
import com.smnotes.presentation.theme.LocalThemeState
import org.koin.androidx.compose.koinViewModel
import com.smnotes.presentation.notesScreen.components.drawer.MainDrawer
import com.smnotes.presentation.theme.Gold
import com.smnotes.presentation.utils.CustomFloatingActionButton
import com.smnotes.presentation.utils.snackbar.NormalSnackbar
import com.smnotes.presentation.utils.snackbar.SnackbarType
import com.smnotes.presentation.utils.snackbar.UndoDeleteSnackbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
@Composable
fun NotesScreen(
    onOpenNote: (id: Long, color: Int) -> Unit,
    viewModel: NotesViewModel = koinViewModel()
) = viewModel.run {
    val state = state.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val clipboardManager = LocalClipboardManager.current
    val themeState = LocalThemeState.current
    val isDark by themeState.isDark

    val drawerState = scaffoldState.drawerState
    var seconds by remember {
        mutableStateOf(4)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        },
        topBar = {
            CustomTopAppBar(title = selectedItemDrawer.value,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
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
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort"
                        )
                    }
                })
        },
        floatingActionButton = {
            CustomFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                icon = Icons.Default.Add
            ) {
                onOpenNote(-1, -1)
            }
        },
        drawerContent = {
            MainDrawer(selected = selectedItemDrawer, onItemSelected = {
                if (selectedItemDrawer != it) {
                    selectedItemDrawer = it
                    onEvent(NotesEvent.GetNotes)
                }
                scope.launch {
                    drawerState.close()
                }
            }, isDark = isDark, toggleLightTheme = themeState.toggleTheme)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Column {

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
                        val dismissState = dismissStateStartEnd(
                            dismissedToStart = {
                                snackbarType = SnackbarType.Delete
                                onEvent(NotesEvent.DeleteNote(note))
                                scope.launch {
                                    seconds = 4
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Note deleted, Retrieve it in ",
                                        actionLabel = "Retrieve"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        onEvent(NotesEvent.RestoreNote)
                                    }
                                }
                            },
                            dismissedToEnd = {
                                snackbarType = SnackbarType.Normal
                                scope.launch {
                                    clipboardManager.setText(AnnotatedString(note.title + "\n" + note.content))
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Copied",
                                    )
                                }
                            })

                        var startAnimation by remember { mutableStateOf(note.important) }

                        val animateColor = animateColorAsState(
                            targetValue = if (startAnimation) Gold else MaterialTheme.colors.background,
                            animationSpec = tween(
                                durationMillis = 500
                            ), label = ""
                        )


                        NoteItem(
                            note = note,
                            animateColor = animateColor.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOpenNote(note.id, note.color)
                                }
                                .animateItem(
                                    tween(500)
                                ),
                            dismissInfo = DismissInfo(
                                dismissState = dismissState,
                                background = {
                                    SwipeBackground()
                                }
                            ),
                            onImportantClick = {
                                onEvent(NotesEvent.ImportantNote(note))
                                startAnimation = !startAnimation
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            if (snackbarType == SnackbarType.Normal) {
                NormalSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onDismiss = {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            } else {

                UndoDeleteSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onPerformAction = {
                        scaffoldState.snackbarHostState.currentSnackbarData?.performAction()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                    seconds = seconds,
                    downSec = { seconds -= 1 }
                )
            }

        }

    }
}