package com.chandra.syncnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chandra.syncnote.mainscreen.CreateNoteScaffold
import com.chandra.syncnote.mainscreen.NoteAppScaffold
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.SyncNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
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


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            NoteAppScaffold(navController)
        }
        composable(Screen.CreateNote.route) {
            CreateNoteScaffold(navController = navController)
        }
    }
}
