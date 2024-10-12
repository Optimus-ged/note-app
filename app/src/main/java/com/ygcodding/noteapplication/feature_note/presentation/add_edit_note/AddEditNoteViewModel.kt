package com.ygcodding.noteapplication.feature_note.presentation.add_edit_note

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ygcodding.noteapplication.feature_note.domain.model.InvalidNoteException
import com.ygcodding.noteapplication.feature_note.domain.model.Note
import com.ygcodding.noteapplication.feature_note.domain.use_case.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _noteColor = MutableStateFlow(Note.noteColors.random().toArgb())
    val noteColor = _noteColor.asStateFlow()

    private val _noteTitle = MutableStateFlow(
        NoteTextFieldState(
            hints = "Enter title"
        )
    )
    val noteTitle = _noteTitle.asStateFlow()

    private val _noteContent = MutableStateFlow(
        NoteTextFieldState(
            hints = "Enter some content"
        )
    )
    val noteContent = _noteContent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != 1) {
                viewModelScope.launch {
                    notesUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = _noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(noteEvent: AddEditNoteEvent) {
        when (noteEvent) {
            is AddEditNoteEvent.EnterTitle -> {
                _noteTitle.value = _noteTitle.value.copy(
                    text = noteEvent.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                    isHintVisible = !noteEvent.focusState.isFocused
                            && _noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnterContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = noteEvent.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !noteEvent.focusState.isFocused
                            && _noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = noteEvent.color
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        notesUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                color = _noteColor.value,
                                timestamp = System.currentTimeMillis(),
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        data object SaveNote : UiEvent()
    }
}