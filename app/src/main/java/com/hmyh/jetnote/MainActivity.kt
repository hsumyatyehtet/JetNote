package com.hmyh.jetnote

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hmyh.jetnote.model.Note
import com.hmyh.jetnote.screen.NoteDetailScreen
import com.hmyh.jetnote.screen.NoteScreen
import com.hmyh.jetnote.screen.NoteViewModel
import com.hmyh.jetnote.ui.theme.JetNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetNoteTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->

                    val noteViewModel: NoteViewModel by viewModels()

                    NotesApp(innerPadding = innerPadding, noteViewModel = noteViewModel)

                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesApp(innerPadding: PaddingValues,noteViewModel: NoteViewModel = viewModel() ){

    val notesList = noteViewModel.getAllNotes()
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    if (selectedNote != null) {
        NoteDetailScreen(
            modifier = Modifier.padding(innerPadding),
            note = selectedNote!!,
            onBackClick = { selectedNote = null }
        )
    } else {
        NoteScreen(
            modifier = Modifier.padding(innerPadding),
            notes = notesList,
            onAddNote = { noteViewModel.addNote(it) },
            onNoteClick = { selectedNote = it },
            onRemoveNote = { noteViewModel.removeNote(it) }
        )
    }

}
