package com.smnotes.domain.usecase

import com.smnotes.domain.repository.NoteRepository
import com.smnotes.domain.model.Note

class GetNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Long): Note? {
        return repository.getNoteById(id)
    }
}