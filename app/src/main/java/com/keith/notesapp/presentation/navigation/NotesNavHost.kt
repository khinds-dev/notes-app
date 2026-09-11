package com.keith.notesapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keith.notesapp.presentation.screen.NoteEditScreen
import com.keith.notesapp.presentation.screen.NoteListScreen

private const val ROUTE_NOTE_LIST = "note_list"
private const val ROUTE_NOTE_EDIT = "note_edit/{noteId}"

@Composable
fun NotesNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_NOTE_LIST) {
        composable(ROUTE_NOTE_LIST) {
            NoteListScreen(
                onNoteClick = { noteId ->
                    navController.navigate("note_edit/$noteId")
                },
                onAddNote = {
                    navController.navigate("note_edit/-1")
                }
            )
        }
        composable(
            route = ROUTE_NOTE_EDIT,
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) {
            NoteEditScreen(onBack = { navController.popBackStack() })
        }
    }
}
