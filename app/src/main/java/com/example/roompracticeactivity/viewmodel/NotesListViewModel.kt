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

    private var _reversedLiveData = MutableLiveData<List<Notes>>()
    val reversedLiveData: LiveData<List<Notes>>
        get() = _reversedLiveData

    private var _staggeredGridLayoutEnable = MutableLiveData<Boolean>(false)
    val staggeredGridLayoutEnable: LiveData<Boolean>
        get() = _staggeredGridLayoutEnable


    init {
        val wordsDao = NotesRoomDatabase.getDatabase(application).notesDao()
        repository = NotesRepository(wordsDao)
        allNotes = repository.allNotes
    }

    fun setOrderAsNewestOnTop() {
        _reversedLiveData.postValue(repository.allNotes.value?.reversed())
    }

    fun setOrderAsOldestOnTop() {
        _reversedLiveData.postValue(allNotes.value)
    }

    fun setStaggeredGridLayoutEnable(value: Boolean) {
        _staggeredGridLayoutEnable.postValue(value)
    }

}