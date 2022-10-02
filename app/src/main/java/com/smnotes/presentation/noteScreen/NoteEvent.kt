package com.smnotes.presentation.noteScreen


sealed class NoteEvent{
    data class EnteredTitle(val value: String): NoteEvent()
    data class EnteredContent(val value: String): NoteEvent()
    data class ChangeColor(val color: Int): NoteEvent()
    object SetImportant : NoteEvent()
    object SaveNote: NoteEvent()
}

