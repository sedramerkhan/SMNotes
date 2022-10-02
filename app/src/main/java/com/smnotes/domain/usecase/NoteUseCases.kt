package com.smnotes.domain.usecase


data class NoteUseCases(
    val getNotes: GetNotes,
    val getImportantNotes: GetImportantNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote
)
