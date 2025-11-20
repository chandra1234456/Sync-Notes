package com.chandra.syncnote

import org.junit.Assert.*
import org.junit.Test

class NotesRepositoryTest {

    @Test
    fun addNote_addsNoteCorrectly() {
        val repo = NotesRepository()

        val note = Note(
            id = 1,
            title = "Test Title",
            content = "Test Content"
        )

        repo.addNote(note)

        val result = repo.getNotes()

        assertEquals(1, result.size)
        assertEquals("Test Title", result[0].title)
    }

    @Test
    fun deleteNote_removesCorrectNote() {
        val repo = NotesRepository()

        repo.addNote(Note(1, "A", "AA"))
        repo.addNote(Note(2, "B", "BB"))

        repo.deleteNote(1)

        val result = repo.getNotes()

        assertEquals(1, result.size)
        assertEquals(2, result[0].id)
    }

    @Test
    fun getNotes_returnsEmptyListInitially() {
        val repo = NotesRepository()

        val result = repo.getNotes()

        assertTrue(result.isEmpty())
    }
}
