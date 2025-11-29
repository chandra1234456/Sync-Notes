package com.chandra.syncnote.github

import android.content.Context
import com.chandra.syncnote.util.Constant
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class GitHubUpdateChecker(private val context: Context) {
    // Replace with your actual GitHub repository details
    private val repositoryOwner = Constant.GITHUB_REPO_OWNER
    private val repositoryName = Constant.GITHUB_REPO_NAME
    private val githubService: GitHubService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
    }
    
    interface GitHubService {
        @GET("repos/{owner}/{repo}/releases/latest")
        suspend fun getLatestRelease(
            @Path("owner") owner: String,
            @Path("repo") repo: String
        ): GitHubRelease
    }
    
    suspend fun checkForUpdate(): UpdateResult {
        return try {
            val latestRelease = githubService.getLatestRelease(repositoryOwner, repositoryName)
            val currentVersion = getCurrentAppVersion()
            val latestVersion = cleanVersion(latestRelease.tagName)
            
            if (isNewVersionAvailable(currentVersion, latestVersion)) {
                UpdateResult.UpdateAvailable(
                    currentVersion = currentVersion,
                    latestVersion = latestVersion,
                    releaseNotes = latestRelease.body,
                    downloadUrl = getApkDownloadUrl(latestRelease),
                    release = latestRelease
                )
            } else {
                UpdateResult.NoUpdate
            }
        } catch (e: Exception) {
            UpdateResult.Error(e.message ?: "Failed to check for updates")
        }
    }
    
    private fun getCurrentAppVersion(): String? {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    }
    
    private fun cleanVersion(version: String): String {
        // Remove 'v' prefix and any extra spaces
        return version.replace("^v".toRegex(), "").trim()
    }
    
    private fun isNewVersionAvailable(current: String?, latest: String): Boolean {
        val currentParts = parseVersionParts(current)
        val latestParts = parseVersionParts(latest)
        
        // Compare major, minor, patch versions
        for (i in 0 until maxOf(currentParts.size, latestParts.size)) {
            val currentPart = currentParts.getOrElse(i) { 0 }
            val latestPart = latestParts.getOrElse(i) { 0 }
            
            if (latestPart > currentPart) return true
            if (latestPart < currentPart) return false
        }
        return false // versions are equal
    }
    
    private fun parseVersionParts(version: String?): List<Int> {
        return version?.split(".")?.map { part ->
            part.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
        }?: emptyList()
    }
    
    private fun getApkDownloadUrl(release: GitHubRelease): String {
        // Find APK asset in release
        val apkAsset = release.assets.find { asset ->
            asset.name.endsWith(".apk", ignoreCase = true)
        }
        return apkAsset?.browserDownloadUrl ?: release.htmlUrl
    }
    
    sealed class UpdateResult {
        object NoUpdate : UpdateResult()
        data class UpdateAvailable(
            val currentVersion: String?,
            val latestVersion: String,
            val releaseNotes: String,
            val downloadUrl: String,
            val release: GitHubRelease
        ) : UpdateResult()
        data class Error(val message: String) : UpdateResult()
    }
    
    data class GitHubRelease(
        @SerializedName("tag_name") val tagName: String,
        @SerializedName("name") val name: String,
        @SerializedName("body") val body: String,
        @SerializedName("html_url") val htmlUrl: String,
        @SerializedName("assets") val assets: List<Asset>
    ) {
        data class Asset(
            @SerializedName("browser_download_url") val browserDownloadUrl: String,
            @SerializedName("name") val name: String
        )
    }
}