package com.chandra.syncnote.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chandra.syncnote.mainscreen.CreateNoteScaffold
import com.chandra.syncnote.mainscreen.EditNoteScaffold
import com.chandra.syncnote.mainscreen.NoteAppScaffold

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateNote : Screen("create_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
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
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType }
            )
        ) {
            EditNoteScaffold(navController = navController)
        }
    }
}
