package com.chandra.syncnote.github.model

import com.chandra.syncnote.github.model.Uploader
import com.google.gson.annotations.SerializedName

data class Asset(
    @SerializedName("browser_download_url")
    var browserDownloadUrl: String?, //APK Location URL
    @SerializedName("content_type")
    var contentType: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("digest")
    var digest: String?,
    @SerializedName("download_count")
    var downloadCount: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("label")
    var label: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("node_id")
    var nodeId: String?,
    @SerializedName("size")
    var size: Int?, //APK SIZE
    @SerializedName("state")
    var state: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("uploader")
    var uploader: Uploader?,
    @SerializedName("url")
    var url: String?
)