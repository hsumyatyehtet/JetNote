package com.hmyh.jetnote.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.hmyh.jetnote.model.Note

class NoteDataSource {

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNotes(): List<Note>{
        return listOf(
            Note(title = "Title 1", description = "Description 1"),
            Note(title = "Title 2", description = "Description 2"),
            Note(title = "Title 3", description = "Description 3"),
            Note(title = "Title 4", description = "Description 4"),
            Note(title = "Title 5", description = "Description 5"),
            Note(title = "Title 6", description = "Description 6"),
            Note(title = "Title 7", description = "Description 7"),
            Note(title = "Title 8", description = "Description 8"),
            Note(title = "Title 9", description = "Description 9"),
            Note(title = "Title 10", description = "Description 10")
        )
    }

}