package com.marwaeltayeb.currencyexchange.data

import android.content.Context
import com.marwaeltayeb.currencyexchange.data.file.CurrencyData
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val context: Context) {

    // Get a list of currency names only
    fun getCurrencyNames(): List<String> {
        val currencyInfo = CurrencyData.getCurrencyInfo()
        return currencyInfo.values.map { it.currencyName }
    }

    // Get currency name by currency code
    fun getCurrencyName(currencyCode: String): String? {
        val currencyInfo = CurrencyData.getCurrencyInfo()
        return currencyInfo[currencyCode]?.currencyName
    }
}
