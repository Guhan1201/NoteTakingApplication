package com.example.roompracticeactivity.database.repository

import androidx.lifecycle.LiveData
import com.example.roompracticeactivity.database.dao.NotesDao
import com.example.roompracticeactivity.database.entities.Notes

class NotesRepository(private val notesDao: NotesDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes()

    suspend fun insert(word: Notes) {
        notesDao.insert(word)
    }


}