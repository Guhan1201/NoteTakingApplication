package com.example.roompracticeactivity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.database.repository.NotesRepository

class NotesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository

    var allNotes: LiveData<List<Notes>>

    var reversedLiveData = MutableLiveData<List<Notes>>()

    init {
        val wordsDao = NotesRoomDatabase.getDatabase(application).notesDao()
        repository = NotesRepository(wordsDao)
        allNotes = repository.allNotes
    }

    fun setOrderAsNewestOnTop() {
        reversedLiveData.postValue(repository.allNotes.value?.reversed())
    }

    fun setOrderAsOldestOnTop() {
        reversedLiveData.postValue(allNotes.value)
    }

}