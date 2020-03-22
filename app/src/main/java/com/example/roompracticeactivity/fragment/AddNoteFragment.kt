package com.example.roompracticeactivity.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.entities.Notes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment() {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)


        val button = view.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            if (TextUtils.isEmpty(notesTitle.text)) {

            } else {
                insertNotes()
            }
            activity?.onBackPressed()
        }
    }

    private fun insertNotes() {
        val timeCreated = System.currentTimeMillis()
        GlobalScope.launch {
            NotesRoomDatabase.getDatabase(context).notesDao().insert(
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

}
