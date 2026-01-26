package com.chandra.syncnote.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

private fun Context.openSettings() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}
