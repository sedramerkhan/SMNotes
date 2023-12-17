package com.smnotes.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.smnotes.data.repository.FakeNoteRepository
import com.smnotes.domain.model.InvalidNoteException
import com.smnotes.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AddNoteTest {
    private lateinit var addNote: AddNote
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        addNote = AddNote(fakeNoteRepository)

    }

    @Test
    fun `Add valid note, inserted correctly`() = runBlocking {
        val note = Note(
            title = "Valid Title",
            content = "Valid Content",
            timestamp = 1,
            color = 1,
        )
        addNote.invoke(note)

        assertThat(fakeNoteRepository.notes.size).isEqualTo(1)
        assertThat(fakeNoteRepository.notes[0]).isEqualTo(note)

    }

    @Test
    fun `Add note with empty title, throw InvalidNoteException`() = runBlocking {
        // Given
        val note = Note("", "Valid Content", timestamp = 1, color = 1)


        // When/Then
        val exception = assertThrows(InvalidNoteException::class.java) {
            runBlocking {
                addNote.invoke(note)
            }
        }
        assertEquals("The title of the note can't be empty.", exception.message)
        assertEquals(0, fakeNoteRepository.notes.size)
    }
    @Test
    fun `Add note with empty content, throw InvalidNoteException`() = runBlocking {
        // Given
        val note = Note("Valid Content", "", timestamp = 1, color = 1)


        // When/Then
        val exception = assertThrows(InvalidNoteException::class.java) {
            runBlocking {
                addNote.invoke(note)
            }
        }
        assertEquals("The content of the note can't be empty.", exception.message)
        assertEquals(0, fakeNoteRepository.notes.size)
    }
}