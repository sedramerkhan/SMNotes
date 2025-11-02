package com.smnotes.di

import androidx.room.Room
import com.smnotes.data.repository.NoteRepositoryImpl
import com.smnotes.data.repository.NoteRepository
import com.smnotes.data.database.NoteDatabase
import com.smnotes.domain.usecase.*
import com.smnotes.presentation.NoteApp
import com.smnotes.presentation.noteScreen.NoteViewModel
import com.smnotes.presentation.notesScreen.NotesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { androidContext() as NoteApp }

    single {
        Room.databaseBuilder(
            androidContext(),
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    single<NoteRepository> {
        NoteRepositoryImpl(get<NoteDatabase>().noteDao())
    }

    single {
        NoteUseCases(
            getNotes = GetNotes(get()),
            getImportantNotes = GetImportantNotes(get()),
            deleteNote = DeleteNote(get()),
            addNote = AddNote(get()),
            getNote = GetNote(get())
        )
    }

    viewModelOf(::NoteViewModel)
    
    viewModel { NotesViewModel(get()) }
}