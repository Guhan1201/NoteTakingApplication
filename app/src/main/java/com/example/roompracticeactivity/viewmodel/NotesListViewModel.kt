package com.example.roompracticeactivity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.database.repository.NotesRepository
import kotlinx.coroutines.launch

class NotesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository

    val allNotes: LiveData<List<Notes>>

    init {
        val wordsDao = NotesRoomDatabase.getDatabase(application).notesDao()
        repository = NotesRepository(wordsDao)
        allNotes = repository.allNotes
    }

    fun insert(notes: Notes) = viewModelScope.launch {
        repository.insert(notes)
    }


}