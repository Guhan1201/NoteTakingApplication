package com.example.roompracticeactivity.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePicker(private val listener: DatePickerDialog.OnDateSetListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calender = Calendar.getInstance()
        val year = calender[Calendar.YEAR]
        val month = calender[Calendar.MONTH]
        val day = calender[Calendar.DAY_OF_MONTH]
        val datePicker = DatePickerDialog(activity!!, listener, year, month, day)
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        return datePicker
    }


}

class TimePicker(private val listener: TimePickerDialog.OnTimeSetListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val hour = calender[Calendar.HOUR_OF_DAY]
        val minute = calender[Calendar.MINUTE]
        return TimePickerDialog(activity!!, listener, hour, minute, false)
    }


}