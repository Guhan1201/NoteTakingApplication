package com.example.roompracticeactivity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Notes(@PrimaryKey @ColumnInfo(name = "note_name") val noteName : String,val description : String)