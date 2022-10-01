package com.smnotes.domain.usecase

import com.smnotes.data.repository.NoteRepository
import com.smnotes.domain.model.Note

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}