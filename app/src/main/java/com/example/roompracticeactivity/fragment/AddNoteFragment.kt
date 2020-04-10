package com.example.roompracticeactivity.fragment

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.config
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.database.repository.NotesRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddNoteFragment : Fragment() {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var repository: NotesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)

        val wordsDao = NotesRoomDatabase.getDatabase(requireContext()).notesDao()
        repository = NotesRepository(wordsDao)


        val button = view.findViewById<LinearLayout>(R.id.save)
        button.setOnClickListener {

            if (TextUtils.isEmpty(notesTitle.text)) {

                val snack = Snackbar.make(
                    view.findViewById(R.id.parent),
                    "Hey, this is a MD2 toast!",
                    Snackbar.LENGTH_LONG
                )
                snack.config(requireContext())
                snack.show()

            } else {
                insertNotes()
                activity?.onBackPressed()

            }
        }
    }

    private fun insertNotes() {
        val timeCreated = System.currentTimeMillis()
        GlobalScope.launch {
            repository.insert(
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
