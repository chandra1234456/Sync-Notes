package com.chandra.syncnote.domain.model

data class NotesFilter(
    var sortBy: SortOption = SortOption.TITLE,      // default: TITLE
    var orderBy: OrderOption = OrderOption.ASCENDING // default: ASCENDING
)