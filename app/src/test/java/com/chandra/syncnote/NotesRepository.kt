package com.chandra.syncnote

class NotesRepository {

    private val notes = mutableListOf<Note>()

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun deleteNote(id: Int) {
        notes.removeIf { it.id == id }
    }

    fun getNotes(): List<Note> = notes
}
