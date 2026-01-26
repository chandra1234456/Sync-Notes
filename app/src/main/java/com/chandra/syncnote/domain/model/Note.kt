package com.chandra.syncnote.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val timeStamp: Long,
    val modifiedDate: Long? = null,
    val isEdited: Boolean = false
)


class InvalidNoteException(message: String) : Exception(message)