package com.smnotes.presentation.noteScreen

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.smnotes.domain.model.Note.Companion.COLORS
import com.smnotes.presentation.noteScreen.components.ColorsDialog
import com.smnotes.presentation.noteScreen.components.TransparentTextField
import com.smnotes.presentation.theme.Gold
import com.smnotes.presentation.utils.CustomFloatingActionButton
import com.smnotes.presentation.utils.snackbar.NormalSnackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@OptIn( ExperimentalMaterialApi::class)
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
    var startAnimation by remember { mutableStateOf(noteImportant) }

    val animateColor = animateColorAsState(
        targetValue = if (startAnimation.value) Gold else Color.Black,
        animationSpec = tween(
            durationMillis = 500
        )
    )




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
                onEvent(NoteEvent.ChangeColor(it.toArgb()))
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
        modifier = Modifier.imePadding() ,
        scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        },
        floatingActionButton = {
            Column(
                modifier = Modifier.navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally) {
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
                    onEvent(NoteEvent.SaveNote)
                }

            }
        },
        ) {

        Box(
            modifier = Modifier
                .fillMaxSize().padding(it)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }

                .background(noteBackgroundAnimatable.value)
                .navigationBarsPadding()
                .padding(vertical = 25.dp, horizontal = 16.dp)
        ) {
            Column(Modifier.padding(top = 16.dp).fillMaxSize()) {
                
               ImportantRow(animateColor.value) {
                   onEvent(NoteEvent.SetImportant)

               }
                TransparentTextField(
                    text = titleState.text,
                    hint = titleState.hint,
                    onValueChange = {
                        onEvent(NoteEvent.EnteredTitle(it))
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
                TransparentTextField(
                    text = contentState.text,
                    hint = contentState.hint,
                    onValueChange = {
                        onEvent(NoteEvent.EnteredContent(it))
                    },
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f),
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


@Composable
fun ImportantRow(
    animateColor : Color,
    onClick : () -> Unit
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically, 
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = "Mark as Important",
            style = MaterialTheme.typography.h4,
            color = Color.Black
        )

        IconButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "important note",
                tint = animateColor
            )
        }
    }

}