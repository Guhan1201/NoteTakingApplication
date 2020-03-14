package com.example.roompracticeactivity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class AddNoteActivity : AppCompatActivity() {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription : EditText
    private lateinit var notesViewModel: NotesListViewModel


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_note)
        notesTitle = findViewById(R.id.edit_word)
        notesDescription = findViewById(R.id.description)


        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            if (TextUtils.isEmpty(notesTitle.text)) {

            } else {
                notesViewModel.insert(Notes(notesTitle.text.toString(),notesDescription.text.toString()))

            }


            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}