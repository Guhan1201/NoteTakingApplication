package com.example.roompracticeactivity.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.fragment.AddNoteFragment.Companion.DATE_TIME_COMPONENT_FORMAT
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.example.roompracticeactivity.workmanager.RemainderWorker
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sasikanth.colorsheet.ColorSheet
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EditNotesFragment : Fragment(R.layout.fragment_edit_notes),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        val NOTES = "notes"
    }

    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var save: FloatingActionButton
    private lateinit var parent: ConstraintLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var notesViewModel: NotesListViewModel
    private lateinit var colorPalette: ImageView
    private lateinit var colors: IntArray
    private lateinit var notes: Notes
    private lateinit var setRemainder: ExtendedFloatingActionButton
    private var calenderScheduled = false
    private lateinit var dateTime: DateTime
    private lateinit var calendar: Calendar


    private var selectedColor: Int = ColorSheet.NO_COLOR


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateTime = DateTime()
        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)
        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)
        toolbar = view.findViewById(R.id.topAppBar)
        parent = view.findViewById(R.id.parent)
        save = view.findViewById(R.id.save)
        colors = resources.getIntArray(R.array.colors)
        colorPalette = view.findViewById(R.id.colorPallete)
        setRemainder = view.findViewById(R.id.setRemainder)
        notes = arguments?.get(NOTES) as Notes
        notesTitle.setText(notes.notesTitle)
        notesDescription.setText(notes.description)
        selectedColor = notes.backgroundColor
        notes.alaram_time?.let {
            val date = notes.alaram_time
            val format = SimpleDateFormat(DATE_TIME_COMPONENT_FORMAT)
            setRemainder.text = format.format(date)
            if (date != null) {
                if (date < System.currentTimeMillis()) {
                    setRemainder.paintFlags = setRemainder.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }

        parent.setBackgroundColor(selectedColor)
        toolbar.setBackgroundColor(selectedColor)
        save.setOnClickListener {
            var updatedNotes = notes.copy(
                notesTitle = notesTitle.text.toString(),
                description = notesDescription.text.toString(),
                backgroundColor = selectedColor,
                last_modified_time = System.currentTimeMillis()
            )
            if (calenderScheduled) {
                updatedNotes = updatedNotes.copy(alaram_time = calendar.timeInMillis)
            }
            notesViewModel.updateNotes(updatedNotes)
            setRemainderNotification()
            backPressed()
        }
        setRemainder.setOnClickListener {
            val datePicker: DialogFragment =
                com.example.roompracticeactivity.picker.DatePicker(this)
            datePicker.show(requireActivity().supportFragmentManager, datePicker.tag)
        }

        colorPalette.setOnClickListener {
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
                R.id.share -> {
                    val shareMsg = notesTitle.text.toString()
                    val intent = ShareCompat.IntentBuilder.from(requireActivity())
                        .setType("text/plain")
                        .setText(shareMsg)
                        .intent

                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setRemainderNotification() {
        val data = Data.Builder()
            .putLong(AddNoteFragment.UNIQUE_ID, notes.createdTime)
            .build()
        if (this::calendar.isInitialized) {
            val timeDiff = calendar.timeInMillis - System.currentTimeMillis()
            val dailyWorkRequest = OneTimeWorkRequest.Builder(RemainderWorker::class.java)
                .setInputData(data)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(requireContext()).enqueue(dailyWorkRequest)
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


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        view?.isShown?.let {
            dateTime = dateTime.copy(year = year, month = month + 1, dayOfMonth = dayOfMonth)
        }
        val timePicker: DialogFragment = com.example.roompracticeactivity.picker.TimePicker(this)
        timePicker.show(requireActivity().supportFragmentManager, timePicker.tag)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SimpleDateFormat")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        view?.isShown?.let {
            dateTime = dateTime.copy(hourOfDay = hourOfDay, minute = minute)
            calendar = Calendar.getInstance()
            calendar.set(
                dateTime.getYear(),
                dateTime.getMonth() - 1,
                dateTime.getDayOfMonth(),
                dateTime.getHourOfDay(),
                dateTime.getMinute()
            )
            val date = calendar.time
            val format = SimpleDateFormat(DATE_TIME_COMPONENT_FORMAT)
            setRemainder.text = format.format(date)
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                setRemainder.paintFlags = setRemainder.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                setRemainder.paintFlags = 0
            }
            calenderScheduled = true
        }

    }
}
