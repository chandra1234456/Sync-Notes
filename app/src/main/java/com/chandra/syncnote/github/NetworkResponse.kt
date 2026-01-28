package com.chandra.syncnote.github

sealed class NetworkResponse<out T> {

    data object Idle : NetworkResponse<Nothing>()

    data object Loading : NetworkResponse<Nothing>()

    data class Success<out T>(val data: String) : NetworkResponse<T>()

    data class Failure(
        val errorCode: Int?,
        val errorMessage: String?,
        val exception: Throwable?
    ) : NetworkResponse<Nothing>()

}