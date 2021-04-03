package com.example.roompracticeactivity.fragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.roompracticeactivity.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditNotesFragment : Fragment(R.layout.fragment_edit_notes) {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var save: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)
        save = view.findViewById(R.id.save)

    }

}