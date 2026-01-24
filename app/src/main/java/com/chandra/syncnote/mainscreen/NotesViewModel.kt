package com.chandra.syncnote.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandra.syncnote.domain.model.InvalidNoteException
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.model.SortOption
import com.chandra.syncnote.domain.model.NotesFilter
import com.chandra.syncnote.domain.model.OrderOption
import com.chandra.syncnote.domain.usecases.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    // Search query
    val searchText = MutableStateFlow("")

    // Sorting/filter options
    private val _notesFilter = MutableStateFlow(
        NotesFilter(sortBy = SortOption.TITLE, orderBy = OrderOption.ASCENDING)
    )
    val notesFilter: StateFlow<NotesFilter> = _notesFilter

    // Reactive combined notes list (search + sort)
    val filteredNotes: StateFlow<List<Note>> = combine(
        _notes, searchText, _notesFilter
    ) { notes, query, filter ->
        var result = notes

        // 1️⃣ Apply search
        if (query.isNotBlank()) {
            result = result.filter { it.title.contains(query, ignoreCase = true) }
        }

        // 2️⃣ Apply sort
        result = sortNotes(result, filter)

        result
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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
                _error.emit(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                noteUseCase.deleteNoteUseCase(note)
            } catch (e: Exception) {
                Log.e("NoteVM", "Delete failed", e)
            }
        }
    }

    fun getNoteById(id: Int, onResult: (Note?) -> Unit) {
        viewModelScope.launch {
            val note = noteUseCase.getNoteByIdUseCase(id)
            onResult(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
         //   noteUseCase.deleteAllNotesUseCase()
        }
    }

    fun restoreNote(note: Note) {
        viewModelScope.launch {
            addNote(note)
            // No need to call getNotes(), flow will update automatically
        }
    }

    // Update the filter
    fun updateFilter(sortBy: SortOption? = null, orderBy: OrderOption? = null) {
        val current = _notesFilter.value
        _notesFilter.value = current.copy(
            sortBy = sortBy ?: current.sortBy,
            orderBy = orderBy ?: current.orderBy
        )
    }

    // Sort helper
    private fun sortNotes(notes: List<Note>, filter: NotesFilter): List<Note> {
        val sortedList = when (filter.sortBy) {
            SortOption.TITLE -> notes.sortedBy { it.title.lowercase() }
            SortOption.CREATED_DATE -> notes.sortedBy { it.timeStamp }
            SortOption.MODIFIED_DATE -> notes.sortedBy { it.modifiedDate }
        }

        return if (filter.orderBy == OrderOption.DESCENDING) {
            sortedList.reversed()
        } else sortedList
    }
}




