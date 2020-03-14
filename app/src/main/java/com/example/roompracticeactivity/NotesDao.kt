package com.example.roompracticeactivity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDao {

    @Query("SELECT * from notes_table ")
    fun getAllNotes() : LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notes:  Notes)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()


}