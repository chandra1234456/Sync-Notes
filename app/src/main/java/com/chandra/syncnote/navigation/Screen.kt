package com.chandra.syncnote.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chandra.syncnote.mainscreen.CreateNoteScaffold
import com.chandra.syncnote.mainscreen.NoteAppScaffold

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateNote : Screen("create_note")
}

sealed class NavigationItem(val route: String) {
    object navCrateHomeScreen : NavigationItem(route = Screen.Home.route)
    object navCrateNoteScreen : NavigationItem(route = Screen.CreateNote.route)
}
@RequiresApi(Build.VERSION_CODES.O)
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
