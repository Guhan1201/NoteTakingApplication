package com.example.roompracticeactivity.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.roompracticeactivity.Notes
import com.example.roompracticeactivity.NotesListViewModel
import com.example.roompracticeactivity.R

class AddNoteFragment : Fragment() {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var notesViewModel: NotesListViewModel


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


        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)


        val button = view.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            if (TextUtils.isEmpty(notesTitle.text)) {

            } else {
                notesViewModel.insert(
                    Notes(
                        notesTitle.text.toString(),
                        notesDescription.text.toString()
                    )
                )

            }
            activity?.onBackPressed()
        }
    }

}
