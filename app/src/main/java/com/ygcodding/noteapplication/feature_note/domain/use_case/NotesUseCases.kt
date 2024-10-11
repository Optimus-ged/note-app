package com.ygcodding.noteapplication.feature_note.domain.use_case

data class NotesUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val getNote: GetNote,
    val addNote: AddNote
)
