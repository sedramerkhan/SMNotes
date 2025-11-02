package com.smnotes.presentation.notesScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smnotes.domain.model.Note
import com.smnotes.domain.usecase.NoteUseCases
import com.smnotes.domain.order.NoteOrder
import com.smnotes.domain.order.OrderType
import com.smnotes.presentation.NoteApp
import com.smnotes.presentation.notesScreen.components.drawer.DrawerItems
import com.smnotes.presentation.utils.snackbar.SnackbarType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteUseCases: NoteUseCases,
    application: NoteApp,
) : AndroidViewModel(application) {

    val application
        get() = getApplication<NoteApp>()

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    var selectedItemDrawer by mutableStateOf(DrawerItems.HOME)

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    var snackbarType by mutableStateOf(SnackbarType.Normal)

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.GetNotes -> {
                getNotesSelectedItemDrawer(state.value.noteOrder)
            }
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotesSelectedItemDrawer(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.ImportantNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(event.note.copy(important = !event.note.important))
                }
            }
        }
    }

    private fun getNotesSelectedItemDrawer(noteOrder: NoteOrder) {
        if (selectedItemDrawer == DrawerItems.HOME)
            getNotes(noteOrder)
        else
            getImportantNotes(noteOrder)
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getImportantNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getImportantNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}