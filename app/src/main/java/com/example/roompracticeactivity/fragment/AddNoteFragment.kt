package com.example.roompracticeactivity.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.workmanager.RemainderWorker
import com.example.roompracticeactivity.config
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.sasikanth.colorsheet.ColorSheet
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddNoteFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        val UNIQUE_ID = "UNIQUE_ID"
        val DATE_TIME_COMPONENT_FORMAT = "MMM dd | hh:mm a"
    }

    private lateinit var notesViewModel: NotesListViewModel
    private lateinit var notesTitle: EditText
    private lateinit var notesDescription: EditText
    private lateinit var parent: ConstraintLayout
    private lateinit var colorPallete: ImageView
    private lateinit var colors: IntArray
    private lateinit var save: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var setRemainder: ExtendedFloatingActionButton
    private lateinit var dateTime: DateTime

    private lateinit var calendar: Calendar
    private var selectedColor: Int = ColorSheet.NO_COLOR
    private lateinit var notes: Notes


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
                snack.show()
            } else {
                insertNotes()
                setRemainderNotification()
                backPressed()
            }
        }
        setRemainder.setOnClickListener {
            val datePicker: DialogFragment =
                com.example.roompracticeactivity.picker.DatePicker(this)
            datePicker.show(requireActivity().supportFragmentManager, datePicker.tag)
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
        dateTime = DateTime()
        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)
        notesTitle = view.findViewById(R.id.edit_word)
        notesDescription = view.findViewById(R.id.description)
        parent = view.findViewById(R.id.parent)
        colors = resources.getIntArray(R.array.colors)
        save = view.findViewById(R.id.save)
        colorPallete = view.findViewById(R.id.colorPallete)
        toolbar = view.findViewById(R.id.topAppBar)
        setRemainder = view.findViewById(R.id.setRemainder)

    }

    private fun insertNotes() {
        val timeCreated = System.currentTimeMillis()
        notes = Notes(
            timeCreated.toString(),
            notesTitle.text.toString(),
            notesDescription.text.toString(),
            timeCreated,
            timeCreated,
            selectedColor,
            if (this::calendar.isInitialized) {
                calendar.timeInMillis
            } else {
                null
            }
        )
        notesViewModel.insertNotes(
            notes
        )
    }

    private fun setRemainderNotification() {
        val data = Data.Builder()
            .putLong(UNIQUE_ID, notes.createdTime)
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
        }

    }
}

data class DateTime(
    private val minute: Int? = null,
    private val hourOfDay: Int? = null,
    private val dayOfMonth: Int? = null,
    private val month: Int? = null,
    private val year: Int? = null
) {
    fun getMinute(): Int = this.minute!!

    fun getYear(): Int = this.year!!

    fun getMonth(): Int = this.month!!

    fun getHourOfDay(): Int = this.hourOfDay!!

    fun getDayOfMonth(): Int = this.dayOfMonth!!
}
