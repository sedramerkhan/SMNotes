package com.smnotes.domain.usecase

import com.smnotes.data.repository.FakeNoteRepository
import com.smnotes.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteNoteTest {
    private lateinit var deleteNote: DeleteNote
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private val note =
        Note(id = 1, title = "Test Title", content = "Test Content", timestamp = 1, color = 1)

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        deleteNote = DeleteNote(fakeNoteRepository)
        runBlocking {
            fakeNoteRepository.insertNote(note)
        }
    }

    @Test
    fun `DeleteNote should remove the note from the repository`() = runBlocking {
        deleteNote(note)

        val result = fakeNoteRepository.getNoteById(note.id)
        assertEquals(null, result)
    }
}