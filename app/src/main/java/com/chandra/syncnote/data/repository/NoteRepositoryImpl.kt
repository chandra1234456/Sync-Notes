package com.chandra.syncnote.data.repository

import com.chandra.syncnote.data.datasource.NotesDao
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val notesDao: NotesDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> = notesDao.getNotes()

    override suspend fun insertNote(note: Note) = notesDao.insertNote(note)

    override suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    override suspend fun getNoteById(id: Int): Note? = notesDao.getNoteById(id)
    override suspend fun deleteAllNote()= notesDao.deleteAllNote()

}