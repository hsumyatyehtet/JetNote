package com.hmyh.jetnote.repository

import com.hmyh.jetnote.data.NoteDatabase
import com.hmyh.jetnote.data.NoteDatabaseDao
import com.hmyh.jetnote.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(private val noteDatabaseDao: NoteDatabaseDao) {

    suspend fun addNote(note: Note) = noteDatabaseDao.insert(note)

    suspend fun updateNote(note: Note) = noteDatabaseDao.update(note)

    suspend fun deleteNote(note: Note) = noteDatabaseDao.deleteNote(note)

    suspend fun deleteAllNotes() = noteDatabaseDao.deleteAll()

    fun getAllNotes(): Flow<List<Note>> = noteDatabaseDao.getNote()
        .flowOn(Dispatchers.IO).conflate()

}