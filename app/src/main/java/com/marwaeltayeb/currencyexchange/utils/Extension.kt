package com.marwaeltayeb.currencyexchange.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.marwaeltayeb.currencyexchange.R

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.showSnackBar(message: String) {
    val snackBar = Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
    snackBar.apply {
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setBackgroundTint(ContextCompat.getColor(context, R.color.grey))
        setAction("OK") {
            dismiss()
        }
        setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        show()
    }
}

