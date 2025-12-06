package com.chandra.syncnote.github

sealed class UpdateUIState {
    object Info : UpdateUIState()
    object Downloading : UpdateUIState()
    object Completed : UpdateUIState()
}
