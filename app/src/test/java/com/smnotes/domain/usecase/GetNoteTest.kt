package com.smnotes.domain.usecase

import com.smnotes.data.repository.FakeNoteRepository
import com.smnotes.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetNoteTest {
    private lateinit var getNote: GetNote
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private val expectedNote = Note(id = 1, title = "Test Title", content = "Test Content", timestamp = 1, color = 1)

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getNote = GetNote(fakeNoteRepository)
        runBlocking {
            fakeNoteRepository.insertNote(expectedNote)
        }
    }

    @Test
    fun `GetNoteById should return the correct note`() = runBlocking {

        val result = getNote(1L)

        assertEquals(expectedNote, result)
    }

    @Test
    fun `getNoteById with non-existing ID should return null`() = runBlocking{
        val nonExistingId = 999L

        val result = getNote(nonExistingId)
        assertEquals(null, result)
    }
}