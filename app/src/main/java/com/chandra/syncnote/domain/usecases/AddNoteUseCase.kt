package com.chandra.syncnote.domain.usecases

import com.chandra.syncnote.domain.model.InvalidNoteException
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.repository.NoteRepository

class AddNoteUseCase(
    private val noteRepository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) throw InvalidNoteException("The Title of note Can't be Empty")
        if (note.content.isBlank()) throw InvalidNoteException("The content of note Can't be Empty")
        return noteRepository.insertNote(note)
    }
}
