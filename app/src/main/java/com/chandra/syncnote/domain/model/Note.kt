package com.chandra.syncnote.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)

class InvalidNoteException(message: String) : Exception(message)