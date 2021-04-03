package com.example.roompracticeactivity.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.config
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.sasikanth.colorsheet.ColorSheet


class AddNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesListViewModel
    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var parent: ConstraintLayout
    private lateinit var colorPallete: ImageView
    private lateinit var colors: IntArray
    private lateinit var save: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar

    private var selectedColor: Int = ColorSheet.NO_COLOR


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        save.setOnClickListener {
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
                backPressed()
            }
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


        super.onStart()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)
        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)
        parent = view.findViewById(R.id.parent)
        colors = resources.getIntArray(R.array.colors)
        save = view.findViewById(R.id.save)
        colorPallete = view.findViewById(R.id.colorPallete)
        toolbar = view.findViewById(R.id.topAppBar)

    }

    private fun insertNotes() {
        val timeCreated = System.currentTimeMillis()

        notesViewModel.insertNotes(
            Notes(
                timeCreated.toString(),
                notesTitle.text.toString(),
                notesDescription.text.toString(),
                timeCreated,
                timeCreated,
                selectedColor
            )
        )
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
