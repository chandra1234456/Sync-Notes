package com.chandra.syncnote

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chandra.syncnote.github.NetworkResponse
import com.chandra.syncnote.github.viewmodel.GithubViewModel
import com.chandra.syncnote.navigation.AppNavHost
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.ui.theme.SyncNoteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
     val githubViewModel: GithubViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Rounded.Add, contentDescription = "Add Note"
                                    )
                                },
                                onClick = {
                                    isChecked = true
                                 // navController.navigate(Screen.CreateNote.route)
                                },
                                expanded = true
                            )
                        }
                        if(isChecked){
                            LaunchedEffect(Unit) {
                                checkApiCall(githubViewModel)
                            }
                        }
                    }) { _ ->
                    AppNavHost(navController = navController)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        githubViewModel.checkLatestApkUpdate()
    }
}
suspend fun checkApiCall(githubViewModel: GithubViewModel) {
   // lifecycleScope.launch {
        //repeatOnLifecycle(Lifecycle.State.STARTED) {
            githubViewModel.checkLatestUpdateAvailable.collectLatest { resp ->
                when (resp) {
                    is NetworkResponse.Idle -> Unit

                    NetworkResponse.Loading -> {
                     //   LoadingDialogHelper.show(this)
                    }

                    is NetworkResponse.Success -> {
                        Log.d("TAG", "checkApiCall: $resp")
                        githubViewModel.resetCheckLatestApkUpdate()
                    }

                    is NetworkResponse.Failure -> {
                        githubViewModel.resetCheckLatestApkUpdate()
                    }
                }
            }
       // }
   // }
}









