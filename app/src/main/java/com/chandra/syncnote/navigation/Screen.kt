package com.chandra.syncnote.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateNote : Screen("create_note")
}

sealed class NavigationItem(val route: String) {
    object navCrateHomeScreen : NavigationItem(route = Screen.Home.route)
    object navCrateNoteScreen : NavigationItem(route = Screen.CreateNote.route)
}