package com.chandra.syncnote.permision

sealed interface PermissionUiState {
    data object Granted : PermissionUiState
    data class Denied(
        val canRequestAgain: Boolean,
        val permanentlyDenied: Boolean
    ) : PermissionUiState
}
