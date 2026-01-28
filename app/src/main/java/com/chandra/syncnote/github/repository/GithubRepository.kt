package com.chandra.syncnote.github.repository


import com.chandra.syncnote.github.ApiService
import com.chandra.syncnote.github.NetworkResponse
import com.chandra.syncnote.github.handleExpection.HandleRetrofitExceptions
import com.chandra.syncnote.github.handleExpection.handleHttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun checkLatestApkVersionAvailable(): NetworkResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLatestRelease()
                if (response.isSuccessful) {
                    val rawBody = response.body().toString()
                    NetworkResponse.Success(rawBody)
                } else {
                    handleHttpException(response)
                }
            } catch (e: Throwable) {
                HandleRetrofitExceptions(e)
            }
        }
    }
}