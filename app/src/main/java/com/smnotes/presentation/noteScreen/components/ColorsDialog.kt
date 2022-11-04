package com.smnotes.presentation.noteScreen.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ColorsDialog(
    onDismiss: () -> Unit,
    colors: List<Color>,
    selectedColor: Int,
    onColorSelected: (Color) -> Unit
) {

    var startAnimation by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        delay(5)
        startAnimation = true
    }
    val scope = rememberCoroutineScope()
    Dialog(
        onDismissRequest = {
            scope.launch {
            startAnimation = false
            delay(300)
            onDismiss()
        } },
        properties = DialogProperties(usePlatformDefaultWidth = false) //full screen dialog
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 60.dp)
        ) {

            AnimatedVisibility(
                visible = startAnimation,
                enter = scaleIn(tween(300)),
                exit = scaleOut(tween(300))
            ) {

                ColorsGrid(
                    colors = colors,
                    selectedColor = selectedColor,
                    onColorSelected = {
                        onColorSelected(it)
                        scope.launch {
                            startAnimation = false
                            delay(200)
                            onDismiss()
                        }

                    }
                )
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorsGrid(
    colors: List<Color>,
    selectedColor: Int,
    onColorSelected: (Color) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(4),
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(colors) { color ->
            val colorInt = color.toArgb()
            ColorItem(
                backgroundColor = color, borderColor =
                if (selectedColor == colorInt) {
                    MaterialTheme.colors.surface
                } else Color.Transparent
            ) {
                onColorSelected(color)
            }
        }

    }
}

@Composable
fun ColorItem(
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .size(50.dp)
            .shadow(1.dp, CircleShape)
            .clip(CircleShape)
            .border(
                width = 4.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable {
                onClick()
            },
        backgroundColor = backgroundColor
    ) {}

}


