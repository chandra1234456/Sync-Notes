package com.chandra.syncnote.github

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class UpdateViewModel(private val context: Context) : ViewModel() {

    var progress by mutableStateOf(0)
    var downloadedMB by mutableStateOf(0)
    var totalMB by mutableStateOf(0)
    var isDownloading by mutableStateOf(false)
    var isCompleted by mutableStateOf(false)

    private val client = OkHttpClient()

    fun startDownload(apkUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                isDownloading = true
                isCompleted = false

                val request = Request.Builder().url(apkUrl).build()
                val response = client.newCall(request).execute()

                val body = response.body ?: return@launch
                val contentLength = body.contentLength()

                totalMB = (contentLength / (1024 * 1024)).toInt()

                val apkFile = File(context.filesDir, "update.apk")
                if (apkFile.exists()) apkFile.delete()

                apkFile.outputStream().use { output ->
                    val buf = ByteArray(8 * 1024)
                    var bytesRead: Int
                    var downloaded = 0L

                    val input = body.byteStream()

                    while (input.read(buf).also { bytesRead = it } != -1) {
                        output.write(buf, 0, bytesRead)
                        downloaded += bytesRead

                        progress = ((downloaded * 100) / contentLength).toInt()
                        downloadedMB = (downloaded / (1024 * 1024)).toInt()
                    }
                }

                isDownloading = false
                isCompleted = true

                installApk(apkFile)

            } catch (e: Exception) {
                isDownloading = false
                e.printStackTrace()
            }
        }
    }

    private fun installApk(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)
    }
}
