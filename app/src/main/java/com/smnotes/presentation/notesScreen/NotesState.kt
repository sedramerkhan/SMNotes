package com.smnotes.presentation.notesScreen

import com.smnotes.domain.model.Note
import com.smnotes.domain.order.NoteOrder
import com.smnotes.domain.order.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
