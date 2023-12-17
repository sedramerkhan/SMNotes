package com.smnotes.data.repository

import com.smnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : NoteRepository {

    val notes = mutableListOf<Note>()
    override fun getNotes(): Flow<List<Note>> {
        return flow { emit(notes) }
    }

    override fun getImportantNotes(): Flow<List<Note>> {
        return flow { emit(notes.filter { it.important } )}
    }

    override suspend fun getNoteById(id: Long): Note? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }
}