package com.chandra.syncnote.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandra.syncnote.domain.model.InvalidNoteException
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.usecases.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteUseCase.getNoteUseCase().collect { list ->
                _notes.value = list
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            try {
                noteUseCase.addNoteUseCase(note)
            } catch (e: InvalidNoteException) {
                _error.value = e.message
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCase.deleteNoteUseCase(note)
        }
    }

    fun getNoteById(id: Int, onResult: (Note?) -> Unit) {
        viewModelScope.launch {
            val note = noteUseCase.getNoteByIdUseCase(id)
            onResult(note)
        }
    }
    var searchText = MutableStateFlow("")
    val filteredNotes = searchText.combine(notes) { query, list ->
        if (query.isBlank()) list
        else list.filter { it.title.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}



