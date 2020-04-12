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
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.config
import com.example.roompracticeactivity.database.NotesRoomDatabase
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.database.repository.NotesRepository
import com.google.android.material.snackbar.Snackbar
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddNoteFragment : Fragment() {

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var repository: NotesRepository
    private lateinit var button: LinearLayout
    private lateinit var back: LinearLayout
    private lateinit var parent: CoordinatorLayout
    private lateinit var colorPallete: ImageView
    private lateinit var colors: IntArray
    private lateinit var parentLinearLayout : LinearLayout

    private var selectedColor: Int = ColorSheet.NO_COLOR
    private var noColorOption = false



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
                backPressed()
            }
        }
        back.setOnClickListener {
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
                        print("Guhan Test$color")
                    })
                .show(requireActivity().supportFragmentManager)
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
        colors = resources.getIntArray(R.array.colors)
        parentLinearLayout = view.findViewById(R.id.parentLinearLayout)


        val wordsDao = NotesRoomDatabase.getDatabase(requireContext()).notesDao()
        repository = NotesRepository(wordsDao)
        button = view.findViewById(R.id.save)
        back = view.findViewById(R.id.back)
        colorPallete = view.findViewById(R.id.colorPallete)

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

    private fun backPressed() {
        activity?.onBackPressed()
    }

    @SuppressLint("ResourceAsColor")
    private fun setColor(@ColorRes color : Int) {
       parentLinearLayout.setBackgroundColor(color)
    }

}
