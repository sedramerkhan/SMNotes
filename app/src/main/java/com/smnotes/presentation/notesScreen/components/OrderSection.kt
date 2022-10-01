package com.smnotes.presentation.notesScreen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.smnotes.domain.order.NoteOrder
import com.smnotes.domain.order.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    val rotationState by animateFloatAsState(
        targetValue = if (noteOrder.orderType is OrderType.Descending) 180f else 0f
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        CustomRadioButton(
            text = "Title",
            selected = noteOrder is NoteOrder.Title,
            onSelected = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) })
        CustomRadioButton(text = "Date", selected = noteOrder is NoteOrder.Date,
            onSelected = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) })
        CustomRadioButton(text = "Color", selected = noteOrder is NoteOrder.Color,
            onSelected = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) })

        Spacer(Modifier.weight(1f))
        IconButton(onClick = {
            val order =
                if (noteOrder.orderType is OrderType.Ascending) OrderType.Descending else OrderType.Ascending
            onOrderChange(noteOrder.copy(order))

        }) {
            Icon(
                imageVector = Icons.Filled.North,
                contentDescription = "order",
                modifier = Modifier
                    .padding(end = 16.dp)
//                        .alpha(ContentAlpha.medium)
                    .rotate(rotationState)
            )
        }
    }
}

@Composable
fun CustomRadioButton(
    text: String,
    selected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    val icon = if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked

    IconToggleButton(
        checked = selected,
        onCheckedChange = onSelected
    )
    {

        Icon(
            imageVector = icon,
            contentDescription = "Radio button icon",
//                    tint = Color(
//                        0xFF9B51E0
//                    )
        )

    }

    Text(
        text = text,
        style = MaterialTheme.typography.h5
    )
    Spacer(modifier = Modifier.width(8.dp))

}