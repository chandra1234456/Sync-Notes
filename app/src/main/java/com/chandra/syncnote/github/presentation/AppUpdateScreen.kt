package com.chandra.syncnote.github.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppUpdateScreen() {
    var showDetails by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (!showDetails) {
        UpdateAvailableScreen(
            onUpdateClick = { 
                // Open Play Store for update
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=com.example.recipefinder")
                    setPackage("com.android.vending")
                }
                context.startActivity(intent)
            },
            onMoreInfoClick = { showDetails = true }
        )
    } else {
        AppDetailsScreen(
            onUpdateClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=com.example.recipefinder")
                    setPackage("com.android.vending")
                }
                context.startActivity(intent)
            }
        )
    }
}

@Composable
@Preview
fun AppUpdateScreenPreview(){
    AppUpdateScreen()
}