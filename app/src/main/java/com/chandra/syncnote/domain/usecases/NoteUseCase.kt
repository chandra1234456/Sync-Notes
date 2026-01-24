package com.chandra.syncnote.domain.usecases

import javax.inject.Inject

data class NoteUseCase @Inject constructor(
    val getNoteUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val deleteAllNotesUseCase: DeleteAllNoteUseCase
)