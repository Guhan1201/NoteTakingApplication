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
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
    private lateinit var button: LinearLayout
    private lateinit var back: LinearLayout
    private lateinit var parent: CoordinatorLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        button.setOnClickListener {
            if (TextUtils.isEmpty(notesTitle.text)) {

                val snack = Snackbar.make(
                    parent,
                    getString(R.string.title_should_not_be_empty),
                    Snackbar.LENGTH_LONG
                )

                snack.config(requireContext())
                snack.show()
            } else {
                insertNotes()
                activity?.onBackPressed()

            }
        }
        back.setOnClickListener {
            activity?.onBackPressed()
        }
        super.onStart()
    }

    override fun onStop() {
        button.setOnClickListener(null)
        back.setOnClickListener(null)
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)
        parent = view.findViewById(R.id.parent)

        val wordsDao = NotesRoomDatabase.getDatabase(requireContext()).notesDao()
        repository = NotesRepository(wordsDao)


        button = view.findViewById(R.id.save)
        back = view.findViewById(R.id.back)

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
