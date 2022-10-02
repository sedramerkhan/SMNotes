package com.smnotes.presentation.notesScreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.smnotes.domain.model.Note
import com.smnotes.presentation.theme.Gold




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    dismissInfo: DismissInfo,
    onImportantClick: () -> Unit,
) {
    var startAnimation by remember { mutableStateOf(note.important) }

    val animateColor = animateColorAsState(
        targetValue = if (startAnimation) Gold else MaterialTheme.colors.background,
        animationSpec = tween(
            durationMillis = 500
        )
    )

    SwipeToDismiss(
        modifier = modifier,
        state = dismissInfo.dismissState,
        directions = dismissInfo.directions,
        background = dismissInfo.background,
        dismissContent = {
            Box {
                CanvasNoteShape(modifier = Modifier.matchParentSize(), color = note.color)
                NoteTitle_Content(title = note.title, content = note.content)
                NoteIcons(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    icon1 = Icons.Default.Star,
                    onClick1 = {
                        onImportantClick()
                        startAnimation = !startAnimation
                    },
                    animateColor = animateColor.value,
                )
            }

        }
    )

}


@Composable
fun CanvasNoteShape(
    modifier: Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    color: Int
) {
    Canvas(modifier = modifier) {
        val clipPath = Path().apply {
            lineTo(size.width - cutCornerSize.toPx(), 0f)
            lineTo(size.width, cutCornerSize.toPx())
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipPath(clipPath) {
            drawRoundRect(
                color = Color(color),
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
            drawRoundRect(
                color = Color(
                    ColorUtils.blendARGB(color, 0x000000, 0.2f)
                ),
                topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
}

@Composable
fun NoteTitle_Content(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(end = 32.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun NoteIcons(
    modifier: Modifier,
    icon1: ImageVector,
    onClick1: () -> Unit,
    animateColor: Color,
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = onClick1,
        ) {
            Icon(
                imageVector = icon1,
                contentDescription = "important note",
                tint = animateColor
            )
        }
    }
}