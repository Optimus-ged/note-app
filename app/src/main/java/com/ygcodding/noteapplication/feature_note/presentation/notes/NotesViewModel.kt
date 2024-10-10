package com.ygcodding.noteapplication.feature_note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ygcodding.noteapplication.feature_note.domain.model.Note
import com.ygcodding.noteapplication.feature_note.domain.use_case.NotesUseCases
import com.ygcodding.noteapplication.feature_note.domain.util.NoteOrder
import com.ygcodding.noteapplication.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    private var _recentlyDeletedNote: Note? = null
    private var _getNotesJob : Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    private fun getNotes(noteOrder: NoteOrder){
        _getNotesJob?.cancel()
         _getNotesJob = viewModelScope.launch {
            notesUseCases.getNotes().onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.launchIn(this)
        }
    }

    fun onEvent(event: NotesEvents) {
        when (event) {
            is NotesEvents.OrderNotes -> {
                if (
                    _state.value.noteOrder::class == event.noteOrder::class &&
                    _state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
            }

            is NotesEvents.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCases.deleteNote(event.note)
                    _recentlyDeletedNote = event.note
                }
            }

            is NotesEvents.RestoreNotes -> {
                viewModelScope.launch {
                    notesUseCases.addNote(_recentlyDeletedNote ?: return@launch)
                    _recentlyDeletedNote = null
                }
            }

            is NotesEvents.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !_state.value.isOrderSectionVisible
                )
            }
        }
    }
}