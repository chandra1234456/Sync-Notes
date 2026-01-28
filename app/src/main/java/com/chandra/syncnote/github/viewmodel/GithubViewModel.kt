package com.chandra.syncnote.github.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandra.syncnote.github.NetworkResponse
import com.chandra.syncnote.github.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _checkLatestUpdateAvailable =
        MutableStateFlow<NetworkResponse<String>>(NetworkResponse.Idle)
    val checkLatestUpdateAvailable: StateFlow<NetworkResponse<String>> = _checkLatestUpdateAvailable

    fun checkLatestApkUpdate() {
        viewModelScope.launch {
            try {
                _checkLatestUpdateAvailable.value = NetworkResponse.Idle
                val result = githubRepository.checkLatestApkVersionAvailable()
                _checkLatestUpdateAvailable.value = result
            } catch (e: Exception) {
                _checkLatestUpdateAvailable.value = NetworkResponse.Failure(null, e.message, e)
            }

        }
    }

    fun resetCheckLatestApkUpdate() {
        _checkLatestUpdateAvailable.value = NetworkResponse.Idle
    }
}