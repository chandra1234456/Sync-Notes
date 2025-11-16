package com.chandra.syncnote.domain.usecases

import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.repository.NoteRepository
import com.chandra.syncnote.util.NoteOrder
import com.chandra.syncnote.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { note ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> note.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> note.sortedBy { it.timeStamp }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> note.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> note.sortedBy { it.timeStamp }
                    }
                }
            }

        }
    }
}