package com.chandra.syncnote.domain.repository

import com.chandra.syncnote.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun getNoteById(id: Int): Note?
}