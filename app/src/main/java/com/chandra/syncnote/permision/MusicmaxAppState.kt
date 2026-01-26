package com.chandra.syncnote.permision

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus

@Stable
class SyncNoteAppState @OptIn(ExperimentalPermissionsApi::class) constructor(
    val permissionState: PermissionState,
    val permissionUiState: PermissionUiState
)

/*
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberSyncNoteAppState(): SyncNoteAppState {
    var isPermissionRequested by rememberSaveable { mutableStateOf(false) }

    val permissionState = rememberSyncNotePermissionState {
        isPermissionRequested = true
    }

    val permissionUiState = when (permissionState.status) {
        PermissionStatus.Granted -> PermissionUiState.Granted

        is PermissionStatus.Denied -> {
            val denied = permissionState.status as PermissionStatus.Denied

            PermissionUiState.Denied(
                canRequestAgain = denied.shouldShowRationale,
                permanentlyDenied = isPermissionRequested && !denied.shouldShowRationale
            )
        }

        else -> {}
    }

    return remember(permissionState, permissionUiState) {
        SyncNoteAppState(
            permissionState = permissionState,
            permissionUiState = permissionUiState as PermissionUiState
        )
    }
}
*/
