package com.chandra.syncnote.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast

fun Context.toastMessage(message: String){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun Context.getAppVersion(): Pair<String, Long> {
    try {
        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        val versionName = packageInfo.versionName

        val versionCode: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        return Pair(versionName, versionCode) as Pair<String, Long>
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return Pair("Unknown", -1L) // Handle the error case
    }
}