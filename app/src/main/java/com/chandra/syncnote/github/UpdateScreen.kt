package com.chandra.syncnote.github

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UpdateScreen(
    update: GitHubUpdateChecker.UpdateResult.UpdateAvailable,
    context: Context,
    onClose: () -> Unit
) {
    val viewModel: UpdateViewModel = viewModel(factory = UpdateVMFactory(context))

    val uiState = when {
        viewModel.isCompleted -> UpdateUIState.Completed
        viewModel.isDownloading -> UpdateUIState.Downloading
        else -> UpdateUIState.Info
    }

    Crossfade(targetState = uiState, animationSpec = tween(600)) { state ->
        when (state) {

            UpdateUIState.Info ->
                UpdateInfoScreen(update, viewModel, onClose)

            UpdateUIState.Downloading ->
                UpdateDownloadingScreen(viewModel)

            UpdateUIState.Completed ->
                UpdateCompletedScreen()
        }
    }
}
