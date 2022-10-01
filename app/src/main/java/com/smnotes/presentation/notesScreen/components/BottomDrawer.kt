package com.smnotes.presentation.noteScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smnotes.domain.order.NoteOrder
import com.smnotes.presentation.notesScreen.components.OrderSection
import com.smnotes.presentation.notesScreen.components.WaveShape

@Composable
fun OrderSectionBottomDrawer(
    noteOrder: NoteOrder,
    onOrderChange: (NoteOrder) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        modifier = Modifier.padding(horizontal = 10.dp),
        elevation = 4.dp
    ) {
        Column {
//            WaveShape()
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                noteOrder = noteOrder,
                onOrderChange = onOrderChange
            )
        }

    }
}