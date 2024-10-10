package com.ygcodding.noteapplication.feature_note.presentation.notes

import com.ygcodding.noteapplication.feature_note.domain.model.Note
import com.ygcodding.noteapplication.feature_note.domain.util.NoteOrder

sealed class NotesEvents {
    data class OrderNotes(val noteOrder: NoteOrder) : NotesEvents()
    data class DeleteNote(val note: Note) : NotesEvents()
    data object RestoreNotes : NotesEvents()
    data object ToggleOrderSection : NotesEvents()
}