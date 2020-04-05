package com.example.roompracticeactivity.customview

import android.R
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText


class CustomEditTextWithBullets(
    context: Context?,
    attrs: AttributeSet?
) : EditText(context, attrs, R.attr.editTextStyle) {

    var number = 1


    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        var text = text
        if (lengthAfter > lengthBefore) {
            if (text.toString().length == 1) {
                text = "$number $text"
                setText(text)
                setSelection(getText().length)
            }
            if (text.toString().endsWith("\n")) {
                number = findFrequency(text.toString())
                val s = StringBuilder(text)
                s.append("$number ")
                text = s
                setText(text)
                number += 1
                setSelection(getText().length)
            }
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    private fun findFrequency(str: String): Int {
        var frequency = 1
        val ch = '\n'
        for (element in str) {
            if (ch == element) {
                ++frequency
            }
        }
        return frequency
    }
}
