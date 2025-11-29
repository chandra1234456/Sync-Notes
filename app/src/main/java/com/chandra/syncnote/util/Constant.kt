package com.chandra.syncnote.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Update

object Constant {
    val bottomItems = listOf(
        "Title" to Icons.Default.Title,
        "Created Date" to Icons.Default.EditCalendar,
        "Modified Date" to Icons.Default.Update
    )
    val orderByItems = listOf(
        "Assending" to Icons.Default.ArrowUpward,
        "Desending" to Icons.Default.ArrowDownward,
    )

    const val GITHUB_REPO_URL = "https://github.com/chandra1234456/Sync-Notes"
    const val GITHUB_REPO_URL_BRANCH_MASTER = "https://github.com/chandra1234456/Sync-Notes/tree/master"
    const val GITHUB_REPO_OWNER = "chandra1234456" //Bala Chandra
    const val GITHUB_REPO_NAME = "Sync-Notes"

}