package com.ygcodding.noteapplication.feature_note.presentation.add_edit_note

data class NoteTextFieldState(
    val text : String = "",
    val hints: String = "" ,
    val isHintVisible : Boolean = true
)
