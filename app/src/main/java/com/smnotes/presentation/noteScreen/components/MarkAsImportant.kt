package com.smnotes.presentation.noteScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MarkAsImportant(
    modifier: Modifier,
    animateColor: Color,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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