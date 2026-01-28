package com.chandra.syncnote.github.model

import com.chandra.syncnote.github.model.Author
import com.google.gson.annotations.SerializedName

data class GetLatestReleaseResponse(
    @SerializedName("assets")
    var assets: List<Asset?>?,
    @SerializedName("assets_url")
    var assetsUrl: String?,
    @SerializedName("author")
    var author: Author?,
    @SerializedName("body")
    var body: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("draft")
    var draft: Boolean?,
    @SerializedName("html_url")
    var htmlUrl: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("immutable")
    var immutable: Boolean?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("node_id")
    var nodeId: String?,
    @SerializedName("prerelease")
    var prerelease: Boolean?,
    @SerializedName("published_at")
    var publishedAt: String?,
    @SerializedName("tag_name")
    var tagName: String?,
    @SerializedName("tarball_url")
    var tarballUrl: String?,
    @SerializedName("target_commitish")
    var targetCommitish: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("upload_url")
    var uploadUrl: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("zipball_url")
    var zipballUrl: String?
)