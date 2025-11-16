package com.chandra.syncnote.domain.usecases

import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}