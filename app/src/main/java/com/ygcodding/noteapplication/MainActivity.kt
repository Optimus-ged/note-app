package com.ygcodding.noteapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ygcodding.noteapplication.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.ygcodding.noteapplication.feature_note.presentation.notes.NotesScreen
import com.ygcodding.noteapplication.feature_note.presentation.util.AddEditNoteScreenRoute
import com.ygcodding.noteapplication.feature_note.presentation.util.NotesScreen
import com.ygcodding.noteapplication.ui.theme.NoteApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteApplicationTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NotesScreen
                    ) {
                        composable<NotesScreen> {
                            NotesScreen(
                                navController = navController
                            )
                        }
                        composable<AddEditNoteScreenRoute> {
                            val args = it.toRoute<AddEditNoteScreenRoute>()
                            AddEditNoteScreen(
                                noteId = args.noteId,
                                noteColor = args.noteColor ?: -1,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}