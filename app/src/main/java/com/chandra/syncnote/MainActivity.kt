package com.chandra.syncnote

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chandra.syncnote.github.GitHubUpdateChecker
import com.chandra.syncnote.navigation.AppNavHost
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.ui.theme.SyncNoteTheme
import dagger.hilt.android.AndroidEntryPoint
import com.chandra.syncnote.util.dialog.startApkDownload
import com.chandra.syncnote.util.dialog.startApkDownloadAndInstall

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var updateChecker: GitHubUpdateChecker

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateChecker = GitHubUpdateChecker(this)

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry.value?.destination?.route
            var isChecked by remember { mutableStateOf(false) }
            SyncNoteTheme {
                Scaffold(
                    floatingActionButton = {
                        if (currentRoute == Screen.Home.route) {
                            ExtendedFloatingActionButton(
                                text = {
                                    Text(
                                        "Add Note",
                                        style = AppBarTypography.labelLarge,
                                        color = Color.Black
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Rounded.Add,
                                        tint = Color.Black, contentDescription = "Add Note"
                                    )
                                },
                                onClick = {
                                    isChecked = true
                                  //navController.navigate(Screen.CreateNote.route)
                                },
                                expanded = true
                            )
                        }
                        if(isChecked){
                            UpdateCheckerScreen(updateChecker)
                        }
                    }) { _ ->
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}
    @Composable
    fun UpdateCheckerScreen(
        updateChecker: GitHubUpdateChecker
    ) {
        var isLoading by remember { mutableStateOf(false) }
        var updateAvailable by remember { mutableStateOf<GitHubUpdateChecker.UpdateResult.UpdateAvailable?>(null) }
        var toastMessage by remember { mutableStateOf<String?>(null) }

        val context = LocalContext.current

        // Trigger the update check when the screen appears
        LaunchedEffect(Unit) {
            isLoading = true
            when (val result = updateChecker.checkForUpdate()) {
                is GitHubUpdateChecker.UpdateResult.UpdateAvailable -> {
                    updateAvailable = result
                }
                is GitHubUpdateChecker.UpdateResult.NoUpdate -> {
                    toastMessage = "You have the latest version!"
                }
                is GitHubUpdateChecker.UpdateResult.Error -> {
                    toastMessage = "Update check failed: ${result.message}"
                }
            }
            isLoading = false
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator()
            }

            // Show Snackbar-like message
            toastMessage?.let { message ->
                LaunchedEffect(message) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    toastMessage = null
                }
            }

            // Show Update Available Dialog
            updateAvailable?.let { update ->
                UpdateDialog(
                    update = update,
                    context = LocalContext.current,
                    onDismiss = { updateAvailable = null }
                )
            }
        }
}

@Composable
fun UpdateDialog(
    update: GitHubUpdateChecker.UpdateResult.UpdateAvailable,          // Your data class with currentVersion, latestVersion, releaseNotes, downloadUrl
    onDismiss: () -> Unit,
    context: Context
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            // Replace with your own icon resource
            Icon(
                imageVector = Icons.Default.SystemUpdate,
                contentDescription = "Update Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "New Version Available!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Current Version: ${update.currentVersion}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Latest Version: ${update.latestVersion}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = update.releaseNotes.take(300) + if (update.releaseNotes.length > 300) "..." else "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
               // startApkDownload(context, update.downloadUrl)
                startApkDownloadAndInstall(context, update.downloadUrl)
                onDismiss()
            }) {
                Text(
                    text = "Update",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Later",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        },
        shape = MaterialTheme.shapes.medium
    )
}






