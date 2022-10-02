package com.smnotes.presentation.noteScreen

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.smnotes.domain.model.Note.Companion.COLORS
import com.smnotes.presentation.noteScreen.components.ColorsDialog
import com.smnotes.presentation.noteScreen.components.TransparentHintTextField
import com.smnotes.presentation.utils.CustomFloatingActionButton
import com.smnotes.presentation.utils.snackbar.NormalSnackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Destination
@Composable
fun NoteScreen(
    navController: NavController,
    noteId: Long,
    noteColor: Int,
    viewModel: NoteViewModel = hiltViewModel()
) = viewModel.run {
    val titleState = noteTitle.value
    val contentState = noteContent.value

    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (colorDialogState) {
        ColorsDialog(
            onDismiss = { colorDialogState = false },
            colors = COLORS,
            selectedColor = viewModel.noteColor.value,
            onColorSelected = {
                scope.launch {
                    noteBackgroundAnimatable.animateTo(
                        targetValue = it,
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                }
                viewModel.onEvent(NoteEvent.ChangeColor(it.toArgb()))
            }
        )
    }
    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is NoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is NoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CustomFloatingActionButton(
                    modifier = Modifier.size(45.dp),
                    icon = Icons.Default.Palette,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    colorDialogState = true
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomFloatingActionButton(
                    icon = Icons.Default.Save,

                    ) {
                    viewModel.onEvent(NoteEvent.SaveNote)
                }

            }
        },
        ) {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(vertical = 25.dp, horizontal = 16.dp)
        ) {
            Column {
                TransparentHintTextField(
                    text = titleState.text,
                    hint = titleState.hint,
                    onValueChange = {
                        viewModel.onEvent(NoteEvent.EnteredTitle(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Next,
                            )
                        },
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                TransparentHintTextField(
                    text = contentState.text,
                    hint = contentState.hint,
                    onValueChange = {
                        viewModel.onEvent(NoteEvent.EnteredContent(it))
                    },
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxHeight(),
                    keyboardOptions = KeyboardOptions(),
                    keyboardActions = KeyboardActions(),
                )
            }
            NormalSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}