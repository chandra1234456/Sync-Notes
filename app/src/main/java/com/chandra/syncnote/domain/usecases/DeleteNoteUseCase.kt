package com.chandra.syncnote.domain.usecases

import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}