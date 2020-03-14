package com.example.roompracticeactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: notesRepository

    val allNotes: LiveData<List<Notes>>

    init {
        val wordsDao = NotesRoomDatabase.getDatabase(application).notesDao()
        repository = notesRepository(wordsDao)
        allNotes = repository.allNotes
    }

    fun insert(notes: Notes) = viewModelScope.launch {
        repository.insert(notes)
    }

}