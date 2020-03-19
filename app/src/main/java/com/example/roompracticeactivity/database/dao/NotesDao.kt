package com.example.roompracticeactivity.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.roompracticeactivity.database.entities.Notes

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes_table ORDER BY last_modified_time DESC")
    fun getAllNotes(): LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notes: Notes)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()


}