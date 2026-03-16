package com.hmyh.jetnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.hmyh.jetnote.screen.NoteScreen
import com.hmyh.jetnote.ui.theme.JetNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetNoteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NoteScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
