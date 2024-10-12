package com.ygcodding.noteapplication.feature_note.presentation.util

import kotlinx.serialization.Serializable

@Serializable
object NotesScreen

@Serializable
data class AddEditNoteScreenRoute(val noteId: Int?, val noteColor: Int?)
