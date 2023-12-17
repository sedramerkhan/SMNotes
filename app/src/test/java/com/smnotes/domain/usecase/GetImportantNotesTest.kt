package com.smnotes.domain.usecase

import com.google.common.truth.Truth
import com.smnotes.data.repository.FakeNoteRepository
import com.smnotes.domain.model.Note
import com.smnotes.domain.order.NoteOrder
import com.smnotes.domain.order.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetImportantNotesTest{
    private lateinit var getImportantNotes: GetImportantNotes 
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getImportantNotes = GetImportantNotes(fakeNoteRepository)


        val notesToInsert = mutableListOf<Note>()
        notesToInsert.addAll(('a'..'z').mapIndexed { index, c ->
            Note(
                title = c.toString(),
                content = c.toString(),
                timestamp = index.toLong(),
                color = index,
                important = arrayOf(false,true).random()
            )

        })

        notesToInsert.shuffle()

        runBlocking {
            notesToInsert.forEach {
                fakeNoteRepository.insertNote(it)
            }
        }

    }

    @Test
    fun `Order important notes by title ascending, correct order`() = runBlocking {
        val notes = getImportantNotes(NoteOrder.Title(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            Truth.assertThat(notes[i].title).isLessThan(notes[i + 1].title)
            Truth.assertThat(notes[i].important).isTrue()
        }
        // Check the last note separately
        if (notes.isNotEmpty()) {
            Truth.assertThat(notes.last().important).isTrue()
        }
    }
    @Test
    fun `Order important notes by title descending, correct order`() = runBlocking {
        val notes = getImportantNotes(NoteOrder.Title(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            Truth.assertThat(notes[i].title).isGreaterThan(notes[i + 1].title)
            Truth.assertThat(notes[i].important).isTrue()
        }
        // Check the last note separately
        if (notes.isNotEmpty()) {
            Truth.assertThat(notes.last().important).isTrue()
        }
    }

    @Test
    fun `Order important notes by date ascending, correct order`() = runBlocking {
        val notes = getImportantNotes(NoteOrder.Date(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            Truth.assertThat(notes[i].timestamp).isLessThan(notes[i + 1].timestamp)
            Truth.assertThat(notes[i].important).isTrue()
        }
        // Check the last note separately
        if (notes.isNotEmpty()) {
            Truth.assertThat(notes.last().important).isTrue()
        }
    }

    @Test
    fun `Order important notes by date descending, correct order`() = runBlocking {
        val notes = getImportantNotes(NoteOrder.Date(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            Truth.assertThat(notes[i].timestamp).isGreaterThan(notes[i + 1].timestamp)
            Truth.assertThat(notes[i].important).isTrue()
        }
        // Check the last note separately
        if (notes.isNotEmpty()) {
            Truth.assertThat(notes.last().important).isTrue()
        }
    }


    @Test
    fun `Order important notes by color ascending, correct order`() = runBlocking {
        val notes = getImportantNotes(NoteOrder.Color(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            Truth.assertThat(notes[i].color).isLessThan(notes[i + 1].color)
            Truth.assertThat(notes[i].important).isTrue()
        }
        // Check the last note separately
        if (notes.isNotEmpty()) {
            Truth.assertThat(notes.last().important).isTrue()
        }
    }

    @Test
    fun `Order important notes by color descending, correct order`() = runBlocking {
        val notes = getImportantNotes(NoteOrder.Color(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            Truth.assertThat(notes[i].color).isGreaterThan(notes[i + 1].color)
            Truth.assertThat(notes[i].important).isTrue()
        }
        // Check the last note separately
        if (notes.isNotEmpty()) {
            Truth.assertThat(notes.last().important).isTrue()
        }
    }

}