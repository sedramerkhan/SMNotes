package com.smnotes.presentation.utils.snackbar

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@SuppressLint("FlowOperatorInvokedInComposition")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UndoDeleteSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onPerformAction: () -> Unit?,
    seconds: Int,
    downSec: () -> Unit,
) {

    LaunchedEffect(key1 = seconds) {

        while (seconds > 0) {
            delay(1000)
            downSec()
        }
    }

    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { data: SnackbarData ->
            val paddingH = 30.dp
            Card(
                shape = RoundedCornerShape(10.dp),
                backgroundColor = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(bottom = 60.dp, start = paddingH, end = paddingH)
                    .fillMaxWidth(),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SnackText(text = data.message)
                    AnimatedContent(
                        targetState = seconds,
                        transitionSpec = {
                            addAnimation().using(
                                SizeTransform(clip = false)
                            )
                        }
                    ) { sec ->
                        SnackText(text = sec.toString())
                    }
                    SnackText(text = ".")
                    Spacer(modifier = Modifier.weight(.1f))
                    data.actionLabel?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.primary.copy(alpha = .7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clickable { onPerformAction() }
                                .padding(8.dp)
                        )
                    }

                }

            }
        }
    )
}

@Composable
fun SnackText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 800): ContentTransform {
    return (slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    )).togetherWith(slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    ))
}