package com.example.roompracticeactivity

import androidx.lifecycle.LiveData

class notesRepository(private val notesDao:  NotesDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes()

    suspend fun insert(word: Notes) {
        notesDao.insert(word)
    }
}