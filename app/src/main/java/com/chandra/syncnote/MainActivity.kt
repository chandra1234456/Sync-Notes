package com.chandra.syncnote

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chandra.syncnote.mainscreen.CreateNoteScaffold
import com.chandra.syncnote.mainscreen.NoteAppScaffold
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.ui.theme.SyncNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SyncNoteTheme {
                Scaffold(floatingActionButton = {
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
                             navController.navigate(Screen.CreateNote.route)
                        },
                        expanded = true
                    )
                }) { _ ->
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
) {
    val showBottomSheet by remember { mutableStateOf(false) }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            NoteAppScaffold(navController /*, onSortClick = {
               // if (showBottomSheet) OpenBottomSheet(showBottomSheet)
            }*/)
        }
        composable(Screen.CreateNote.route) {
            CreateNoteScaffold(navController = navController)
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenBottomSheet(showBottomSheet: Boolean) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }) {
                Text("Hide bottom sheet")
            }
        }
}*/
