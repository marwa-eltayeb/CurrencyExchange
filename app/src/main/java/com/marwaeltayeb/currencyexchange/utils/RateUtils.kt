package com.marwaeltayeb.currencyexchange.utils

import com.marwaeltayeb.currencyexchange.R

class RateUtils {
    companion object {

        fun getCodeName(key: String): String {
            when (key) {
                "EUR" -> return "Euro"
                "USD" -> return "US Dollar"
                "CAD" -> return "Marwa"
                "HKD" -> return "Marwa"
                "ISK" -> return "Marwa"
                "PHP" -> return "Marwa"
                "DKK" -> return "Marwa"
                "HUF" -> return "Marwa"
                "CZK" -> return "Marwa"
                "AUD" -> return "Marwa"
                "RON" -> return "Marwa"
                "SEK" -> return "Marwa"
                "IDR" -> return "Marwa"
                "INR" -> return "Marwa"
                "BRL" -> return "Marwa"
                "RUB" -> return "Marwa"
                "HRK" -> return "Marwa"
                "JPY" -> return "Marwa"
                "THB" -> return "Marwa"
                "CHF" -> return "Marwa"
                "SGD" -> return "Marwa"
                "PLN" -> return "Marwa"
                "BGN" -> return "Marwa"
                "TRY" -> return "Marwa"
                "CNY" -> return "Marwa"
                "NOK" -> return "Marwa"
                "NZD" -> return "Marwa"
                "ZAR" -> return "Marwa"
                "MXN" -> return "Marwa"
                "ILS" -> return "Marwa"
                "GBP" -> return "Marwa"
                "KRW" -> return "Marwa"
                else -> return "Nothing"
            }
        }

        fun getFlag(key: String): Int {
            when (key) {
                "EUR" -> return R.drawable.flag_euro
                "USD" -> return R.drawable.flag_dollar
                "CAD" -> return R.drawable.ic_launcher_background
                "HKD" -> return R.drawable.ic_launcher_background
                "ISK" -> return R.drawable.ic_launcher_background
                "PHP" -> return R.drawable.ic_launcher_background
                "DKK" -> return R.drawable.ic_launcher_background
                "HUF" -> return R.drawable.ic_launcher_background
                "CZK" -> return R.drawable.ic_launcher_background
                "AUD" -> return R.drawable.ic_launcher_background
                "RON" -> return R.drawable.ic_launcher_background
                "SEK" -> return R.drawable.ic_launcher_background
                "IDR" -> return R.drawable.ic_launcher_background
                "INR" -> return R.drawable.ic_launcher_background
                "BRL" -> return R.drawable.ic_launcher_background
                "RUB" -> return R.drawable.ic_launcher_background
                "HRK" -> return R.drawable.ic_launcher_background
                "JPY" -> return R.drawable.ic_launcher_background
                "THB" -> return R.drawable.ic_launcher_background
                "CHF" -> return R.drawable.ic_launcher_background
                "SGD" -> return R.drawable.ic_launcher_background
                "PLN" -> return R.drawable.ic_launcher_background
                "BGN" -> return R.drawable.ic_launcher_background
                "TRY" -> return R.drawable.ic_launcher_background
                "CNY" -> return R.drawable.ic_launcher_background
                "NOK" -> return R.drawable.ic_launcher_background
                "NZD" -> return R.drawable.ic_launcher_background
                "ZAR" -> return R.drawable.ic_launcher_background
                "MXN" -> return R.drawable.ic_launcher_background
                "ILS" -> return R.drawable.ic_launcher_background
                "GBP" -> return R.drawable.ic_launcher_background
                "KRW" -> return R.drawable.ic_launcher_background
                else -> return R.drawable.ic_launcher_background
            }
        }
    }
}