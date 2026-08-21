package com.marwaeltayeb.currencyexchange.core.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.data.file.CurrencyData

object CurrencyUtils {

    fun updateCurrencyUi(
        currencyCode: String,
        flagImageView: ImageView,
        currencyCodeTextView: TextView? = null,
        currencyNameTextView: TextView? = null,
        context: Context
    ) {
        // Access the currencyInfo map directly from the singleton
        val currencyInfo = CurrencyData.getCurrencyInfo()
        val currency = currencyInfo[currencyCode]

        // Check if currency info is available
        if (currency != null) {
            // Load the flag using Glide
            Glide.with(context)
                .load(currency.flagUrl)
                .placeholder(R.drawable.ic_flag)
                .into(flagImageView)

            // Update currency code TextView if provided
            currencyCodeTextView?.text = currencyCode

            // Update currency name TextView if provided
            currencyNameTextView?.text = currency.currencyName
        }
    }
}