package com.smnotes.presentation.notesScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.smnotes.presentation.notesScreen.NotesEvent
import kotlinx.coroutines.launch

data class DismissInfo @OptIn(ExperimentalMaterialApi::class) constructor(
    val dismissState: DismissState,
    val directions: Set<DismissDirection> = setOf(
        DismissDirection.StartToEnd,
        DismissDirection.EndToStart
    ),
    val background: @Composable (RowScope.() -> Unit) = {},
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBackground() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.CopyAll,
            contentDescription = "copy",
        )

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete note",
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissState_Start_End(DismissedToStart : ()-> Unit, DismissedToEnd :() -> Unit) = rememberDismissState {
    when (it) {
        DismissValue.DismissedToStart -> {
           DismissedToStart()
        }
        DismissValue.DismissedToEnd -> {
          DismissedToEnd()
        }
        else -> {
        }
    }
    false
}