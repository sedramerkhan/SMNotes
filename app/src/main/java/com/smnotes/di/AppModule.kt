package com.smnotes.di

import androidx.room.Room
import com.smnotes.data.repository.AuthRepositoryImpl
import com.smnotes.data.auth.SessionStore
import com.smnotes.data.database.NoteDatabase
import com.smnotes.data.network.NetworkMonitor
import com.smnotes.data.remote.provideHttpClient
import com.smnotes.data.remote.datasource.AuthRemoteDataSource
import com.smnotes.data.remote.datasource.AuthRemoteDataSourceImpl
import com.smnotes.data.remote.datasource.NoteRemoteDataSource
import com.smnotes.data.remote.datasource.NoteRemoteDataSourceImpl
import com.smnotes.data.repository.NoteRepositoryImpl
import com.smnotes.domain.repository.NoteRepository
import com.smnotes.domain.repository.AuthRepository
import com.smnotes.domain.sync.SyncManager
import com.smnotes.domain.usecase.*
import com.smnotes.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.smnotes.presentation.NoteApp
import com.smnotes.presentation.authScreen.AuthViewModel
import com.smnotes.presentation.noteScreen.NoteViewModel
import com.smnotes.presentation.notesScreen.NotesViewModel
import com.smnotes.presentation.splashScreen.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val BASE_URL get() = BuildConfig.BASE_URL

val databaseModule = module {
    single { androidContext() as NoteApp }

    single {
        Room.databaseBuilder(
            androidContext(),
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).addMigrations(NoteDatabase.MIGRATION_1_2, NoteDatabase.MIGRATION_2_3).build()
    }

    single { get<NoteDatabase>().noteDao() }
}

val networkModule = module {
    single { SessionStore(androidContext()) }
    single { provideHttpClient(get(), BASE_URL) }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get(), BASE_URL) }
    single<NoteRemoteDataSource> { NoteRemoteDataSourceImpl(get(), BASE_URL) }
    single { NetworkMonitor(androidContext()) }
}

val domainModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single { SyncManager(get(), get(), get()) }
    single<NoteRepository> { NoteRepositoryImpl(get(), get(), get(), get(), get()) }

    single {
        NoteUseCases(
            getNotes = GetNotes(get()),
            getImportantNotes = GetImportantNotes(get()),
            deleteNote = DeleteNote(get()),
            addNote = AddNote(get()),
            getNote = GetNote(get())
        )
    }

    single {
        AuthUseCases(
            login = Login(get()),
            register = Register(get()),
            logout = Logout(get()),
            isLoggedIn = IsLoggedIn(get()),
            getLoggedInEmail = GetLoggedInEmail(get()),
            tryRestoreSession = TryRestoreSession(get())
        )
    }
}

val presentationModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::NoteViewModel)
    viewModel { NotesViewModel(get(), get(), get(), get()) }
    viewModelOf(::AuthViewModel)
}

val appModule = listOf(databaseModule, networkModule, domainModule, presentationModule)
