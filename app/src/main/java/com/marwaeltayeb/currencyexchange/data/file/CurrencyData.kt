package com.marwaeltayeb.currencyexchange.data.file

import android.content.Context
import com.marwaeltayeb.currencyexchange.data.model.Currency

object CurrencyData {

    private var currencyInfo: Map<String, Currency>? = null

    @Synchronized
    fun initialize(context: Context) {
        if (currencyInfo == null) {
            currencyInfo = CurrencyLoader.loadCurrencyInfo(context)
        }
    }

    fun getCurrencyInfo(): Map<String, Currency> {
        return currencyInfo ?: throw IllegalStateException("CurrencyData not initialized. Call initialize() first.")
    }
}