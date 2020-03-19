package com.example.roompracticeactivity.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Notes(
    @PrimaryKey
    @ColumnInfo(name = "notes_uid")
    val notesUid: String,

    @ColumnInfo(name = "notes_title")
    val notesTitle: String,

    @ColumnInfo(name = "note_description")
    val description: String,

    @NonNull
    @ColumnInfo(name = "created_time")
    val createdTime: Long,

    @ColumnInfo(name = "last_modified_time")
    val last_modified_time: Long

)