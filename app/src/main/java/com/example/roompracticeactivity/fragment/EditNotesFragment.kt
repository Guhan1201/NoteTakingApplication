package com.example.roompracticeactivity.fragment

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.fragment.AddNoteFragment.Companion.DATE_TIME_COMPONENT_FORMAT
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sasikanth.colorsheet.ColorSheet
import java.text.SimpleDateFormat

class EditNotesFragment : Fragment(R.layout.fragment_edit_notes) {

    companion object {
        val NOTES = "notes"
    }

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var save: FloatingActionButton
    private lateinit var parent: ConstraintLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var notesViewModel: NotesListViewModel
    private lateinit var colorPallete: ImageView
    private lateinit var colors: IntArray
    private lateinit var notes: Notes
    private lateinit var setRemainder: ExtendedFloatingActionButton

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
        setRemainder = view.findViewById(R.id.setRemainder)
        notes = arguments?.get(NOTES) as Notes
        notesTitle.setText(notes.notesTitle)
        notesDescription.setText(notes.description)
        selectedColor = notes.backgroundColor
        notes.alaram_time?.let {
            val date = notes.alaram_time
            val format = SimpleDateFormat(DATE_TIME_COMPONENT_FORMAT)
            setRemainder.text = format.format(date)
            setRemainder.paintFlags= setRemainder.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        parent.setBackgroundColor(selectedColor)
        toolbar.setBackgroundColor(selectedColor)
        save.setOnClickListener {
            val updatedNotes = notes.copy(
                notesTitle = notesTitle.text.toString(),
                description = notesDescription.text.toString(),
                backgroundColor = selectedColor,
                last_modified_time = System.currentTimeMillis()
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

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    notesViewModel.deleteNotes(notes = notes)
                    backPressed()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    private fun backPressed() {
        findNavController().navigateUp()
    }

    @SuppressLint("ResourceAsColor")
    private fun setColor(@ColorRes color: Int) {
        parent.setBackgroundColor(color)
        toolbar.setBackgroundColor(color)
    }


}