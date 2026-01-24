package com.chandra.syncnote.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chandra.syncnote.domain.model.Note


@Database(
    entities = [Note::class],
    version = 2
)
abstract class SyncNoteDatabase: RoomDatabase() {
    abstract val notesDao : NotesDao
    companion object{
        const val DATA_BASE_NAME= "SyncNote"
    }
}