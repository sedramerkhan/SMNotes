package com.smnotes.data.repository

import com.smnotes.data.database.NoteDao
import com.smnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override fun getImportantNotes(): Flow<List<Note>> {
        return dao.getImportantNotes()
    }

    override suspend fun getNoteById(id: Long): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}