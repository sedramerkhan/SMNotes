package com.smnotes.presentation.utils.snackbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@ExperimentalMaterialApi
@Composable
fun NormalSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?
) {

    val shape = CircleShape
    val backgroundColor = MaterialTheme.colors.surface
    val contentColor = MaterialTheme.colors.onSurface
    val textStyle = MaterialTheme.typography.h6

    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { data: SnackbarData ->
            Card(
                shape = shape,
                backgroundColor = backgroundColor,
                modifier = Modifier
                    .padding(vertical = 40.dp, horizontal = 60.dp)
                    .wrapContentSize(),
                elevation = 4.dp
            ) {
                Text(
                    text = data.message,
                    style = textStyle,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    )
}