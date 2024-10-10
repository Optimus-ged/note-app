package com.ygcodding.noteapplication.feature_note.domain.use_case

import com.ygcodding.noteapplication.feature_note.domain.model.InvalidNoteException
import com.ygcodding.noteapplication.feature_note.domain.model.Note
import com.ygcodding.noteapplication.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class AddNote @Inject constructor(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()){
            throw InvalidNoteException("The title should not be empty")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("The content should not be empty")
        }
        return repository.insertNote(note)
    }
}