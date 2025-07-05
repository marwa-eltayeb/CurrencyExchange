package com.marwaeltayeb.currencyexchange.data.file

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marwaeltayeb.currencyexchange.data.model.Currency

object CurrencyLoader {

    fun loadCurrencyInfo(context: Context): Map<String, Currency> {
        val json = context.assets.open("currency_flags.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<Map<String, Currency>>() {}.type
        return Gson().fromJson(json, type)
    }

}