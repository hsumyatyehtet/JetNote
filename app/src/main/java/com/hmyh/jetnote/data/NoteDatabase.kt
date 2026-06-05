package com.hmyh.jetnote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hmyh.jetnote.model.Note

@TypeConverters(Converters::class)
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDatabaseDao(): NoteDatabaseDao

}
