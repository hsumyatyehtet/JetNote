package com.hmyh.jetnote.di

import android.content.Context
import androidx.room.Room
import com.hmyh.jetnote.data.NoteDatabase
import com.hmyh.jetnote.data.NoteDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideNoteDatabaseDao(noteDatabase: NoteDatabase): NoteDatabaseDao =
        noteDatabase.noteDatabaseDao()


    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(
                context = context,
                NoteDatabase::class.java,
                "notes_db"
            ).fallbackToDestructiveMigration(false).build()


}