package com.ygcodding.noteapplication.feature_note.domain.use_case

import com.ygcodding.noteapplication.feature_note.domain.model.Note
import com.ygcodding.noteapplication.feature_note.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        return repository.deleteNote(note)
    }
}