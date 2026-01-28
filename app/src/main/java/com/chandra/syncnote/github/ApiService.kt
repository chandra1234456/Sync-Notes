package com.chandra.syncnote.github

import com.chandra.syncnote.github.model.GetLatestReleaseResponse
import com.chandra.syncnote.util.Constant.GITHUB_REPO_NAME
import com.chandra.syncnote.util.Constant.GITHUB_REPO_OWNER
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("${GITHUB_REPO_OWNER}${GITHUB_REPO_NAME}releases/latest")
    suspend fun getLatestRelease(): Response<GetLatestReleaseResponse>


}
