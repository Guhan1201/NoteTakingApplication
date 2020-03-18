package com.example.roompracticeactivity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.entities.Notes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddNoteActivity : AppCompatActivity() {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription : EditText


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_note)
        notesTitle = findViewById(R.id.edit_word)
        notesDescription = findViewById(R.id.description)


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            if (TextUtils.isEmpty(notesTitle.text)) {

            } else {
                insertNotes()
            }
            finish()
        }
    }

    private fun insertNotes() {
        val timeCreated = System.currentTimeMillis()
        GlobalScope.launch {
            NotesRoomDatabase.getDatabase(this@AddNoteActivity).notesDao().insert(
                Notes(
                    timeCreated.toString(),
                    notesTitle.text.toString(),
                    notesDescription.text.toString(),
                    timeCreated,
                    timeCreated
                )
            )
        }

    }
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}