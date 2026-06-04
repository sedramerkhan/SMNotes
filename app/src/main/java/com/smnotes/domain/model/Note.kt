package com.smnotes.domain.model

data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Long,
    val isImportant: Boolean = false,
    val id: Long = 0,
)

class InvalidNoteException(message: String) : Exception(message)
