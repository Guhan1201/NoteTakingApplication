package com.example.roompracticeactivity.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.dao.NotesDao
import com.example.roompracticeactivity.database.entities.Notes

class NotesRepository(
    val application: Application,
    private val notesDao: NotesDao = NotesRoomDatabase.getDatabase(application).notesDao()
) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes()

    suspend fun insert(word: Notes) {
        notesDao.insert(word)
    }

    suspend fun delete(id: String) {
        notesDao.delete(id)
    }

    suspend fun update(notes: Notes) {
        notesDao.update(notes)
    }


}