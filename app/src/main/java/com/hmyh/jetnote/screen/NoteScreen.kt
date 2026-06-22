package com.hmyh.jetnote.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmyh.jetnote.R
import com.hmyh.jetnote.components.NoteButton
import com.hmyh.jetnote.components.NoteInputText
import com.hmyh.jetnote.data.NoteDataSource
import com.hmyh.jetnote.model.Note
import com.hmyh.jetnote.util.fromDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    modifier: Modifier,
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onNoteClick: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = modifier.padding(8.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.app_name)
                )
            },
            actions = {
                Icon(
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Icon"
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0XFFDADFE3)
            )
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteInputText(
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 8.dp
                ),
                text = title,
                label = "Title",
                onTextChange = {
                    if (it.all { char->
                        char.isLetter() || char.isWhitespace()
                        })title = it
                }
            )

            NoteInputText(
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 8.dp
                ),
                text = description,
                label = "Add a note",
                onTextChange = {
                    if (it.all { char->
                        char.isLetter() || char.isWhitespace()
                        })description = it
                }
            )

            NoteButton(
                text = "Save",
                onClick = {

                    if (title.isNotEmpty() && description.isNotEmpty()){
                        onAddNote(
                            Note(
                            title = title,
                            description = description))

                        title = ""
                        description=""

                        Toast.makeText(context,"Note Added", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(10.dp)
        )

        LazyColumn {
            items(notes, key = { it.id }) { note ->
                NoteRow(
                    note = note,
                    onNoteClick = onNoteClick,
                    onRemoveNote = onRemoveNote
                )
            }
        }

    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 28.dp))
            .fillMaxWidth(),
        color = Color(0xFFDFE6EB),
        shadowElevation = 4.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNoteClick(note) }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = note.title, style = MaterialTheme.typography.titleLarge)
                Text(text = note.description, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = fromDate(note.entryDate.time),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { onRemoveNote(note) }) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete note"
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun NoteScreenPreview(){
    NoteScreen(
        modifier = Modifier,
        notes = NoteDataSource().loadNotes(),
        onAddNote = {},
        onNoteClick = {},
        onRemoveNote = {}
    )
}