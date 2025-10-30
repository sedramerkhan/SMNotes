package com.smnotes.presentation.noteScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smnotes.domain.model.InvalidNoteException
import com.smnotes.domain.model.Note
import com.smnotes.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
        hint = "Enter title..."
    )
    )

    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
        hint = "Enter some content"
    )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.COLORS.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _noteImportant = mutableStateOf(false)
    val noteImportant: State<Boolean> = _noteImportant

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Long = 0

    var colorDialogState by mutableStateOf(false)

    fun loadNote(noteId: Long) {
        if(noteId != -1L) {
            viewModelScope.launch {
                noteUseCases.getNote(noteId)?.also { note ->
                    currentNoteId = note.id
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                    )
                    _noteContent.value = _noteContent.value.copy(
                        text = note.content,
                    )
                    _noteColor.value = note.color
                    _noteImportant.value = note.important
                }
            }
        } else {
            // New note: reset state to defaults
            currentNoteId = 0
            _noteTitle.value = NoteTextFieldState(hint = "Enter title...")
            _noteContent.value = NoteTextFieldState(hint = "Enter some content")
            _noteColor.value = Note.COLORS.random().toArgb()
            _noteImportant.value = false
        }
    }

    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.SetImportant -> {
                _noteImportant.value = !noteImportant.value
            }
            is NoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is NoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }
            is NoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is NoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                important =  noteImportant.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}