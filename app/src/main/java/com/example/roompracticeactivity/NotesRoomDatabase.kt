package com.example.roompracticeactivity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Notes::class], version = 1, exportSchema = false)
public abstract class NotesRoomDatabase : RoomDatabase() {

    abstract fun notesDao() : NotesDao


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var notesDao = database.notesDao()

                    // Delete all content here.
                    notesDao.deleteAll()

                    // Add sample words.
                    var notes = Notes("test1", "test1test1test1test1test1test1test1test1test1test1test1test1test1")
                    notesDao.insert(notes)
                    notes = Notes("test2","test2test2test2test2test2test2test2test2test2test2test2")
                    notesDao.insert(notes)

                    // TODO: Add your own words!
                    notes = Notes("TODO!3","TODO!3TODO!3TODO!3TODO!3TODO!3TODO!3TODO!3TODO!3")
                    notesDao.insert(notes)
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: NotesRoomDatabase? = null

        fun getDatabase(context: Context): NotesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}