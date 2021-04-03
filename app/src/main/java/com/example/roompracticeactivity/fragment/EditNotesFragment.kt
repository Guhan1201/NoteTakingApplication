package com.example.roompracticeactivity.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.notes_list_fragment.*

class EditNotesFragment : Fragment(R.layout.fragment_edit_notes) {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var save: FloatingActionButton
    private lateinit var parent: ConstraintLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var notesViewModel: NotesListViewModel
    private lateinit var colorPallete: ImageView
    private lateinit var colors: IntArray

    private var selectedColor: Int = ColorSheet.NO_COLOR


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)
        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)
        toolbar = view.findViewById(R.id.topAppBar)
        parent = view.findViewById(R.id.parent)
        save = view.findViewById(R.id.save)
        colors = resources.getIntArray(R.array.colors)
        colorPallete = view.findViewById(R.id.colorPallete)
        val notes = arguments?.get("notes") as Notes
        notesTitle.setText(notes.notesTitle)
        notesDescription.setText(notes.description)
        selectedColor = notes.backgroundColor
        parent.setBackgroundColor(selectedColor)
        toolbar.setBackgroundColor(selectedColor)
        save.setOnClickListener {
            val updatedNotes = notes.copy(
                notesTitle = notesTitle.text.toString(),
                description = notesDescription.text.toString(),
                backgroundColor = selectedColor
            )
            notesViewModel.updateNotes(updatedNotes)
            backPressed()
        }
        colorPallete.setOnClickListener {
            ColorSheet().cornerRadius(8)
                .colorPicker(
                    colors = colors,
                    noColorOption = true,
                    selectedColor = selectedColor,
                    listener = { color ->
                        selectedColor = color
                        setColor(color)
                    })
                .show(requireActivity().supportFragmentManager)
        }

    }

    private fun backPressed() {
        requireActivity().onBackPressed()
    }

    @SuppressLint("ResourceAsColor")
    private fun setColor(@ColorRes color: Int) {
        parent.setBackgroundColor(color)
        toolbar.setBackgroundColor(color)
    }


}