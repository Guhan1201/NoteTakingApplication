package com.example.roompracticeactivity

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Snackbar.config(context: Context) {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params
    this.view.background = context.getDrawable(R.drawable.custom_snack_bar)
    ViewCompat.setElevation(this.view, 6f)
}