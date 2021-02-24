package com.marwaeltayeb.currencyexchange.utils

import android.widget.ListView

interface DialogCallback {
    fun onCallback(listView : ListView, item : Int)
}