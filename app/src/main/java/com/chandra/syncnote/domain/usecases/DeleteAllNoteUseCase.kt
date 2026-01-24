package com.chandra.syncnote.domain.usecases

import com.chandra.syncnote.domain.repository.NoteRepository

class DeleteAllNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(function: () -> Unit) {
        repository.deleteAllNote()
    }
}