package com.smnotes.presentation.notesScreen.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smnotes.domain.order.NoteOrder
import com.smnotes.domain.order.OrderType
import com.smnotes.presentation.notesScreen.components.DefaultRadioButton

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            CustomRadioButton(text = "Title", selected =  noteOrder is NoteOrder.Title, onSelected = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) })
            CustomRadioButton(text = "Date", selected =  noteOrder is NoteOrder.Date,
                onSelected = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) })
            CustomRadioButton(text = "Color", selected =  noteOrder is NoteOrder.Color,
                onSelected = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) })

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            ) {
            CustomRadioButton(text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelected = {
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                }
            )

            CustomRadioButton( text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelected = {
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                })

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
                imageVector =icon,
                contentDescription = "Radio button icon",
//                    tint = Color(
//                        0xFF9B51E0
//                    )
            )

    }

    Text(text = text,
    style = MaterialTheme.typography.h5)
    Spacer(modifier = Modifier.width(8.dp))

}