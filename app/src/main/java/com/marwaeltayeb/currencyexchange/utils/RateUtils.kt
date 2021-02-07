package com.marwaeltayeb.currencyexchange.utils

import com.marwaeltayeb.currencyexchange.R

class RateUtils {
    companion object {

        fun getCodeName(key: String): String {
            when (key) {
                "BGN" -> return "Marwa"
                "GBP" -> return "Eltayeb"
                "EUR" -> return "Euro"
                "USD" -> return "US Dollar"
            }
            return "Nothing"
        }

        fun getFlag(key: String): Int {
            when (key) {
                "BGN" -> return R.drawable.ic_launcher_background
                "GBP" -> return R.drawable.ic_launcher_background
                "EUR" -> return R.drawable.flag_euro
                "USD" -> return R.drawable.flag_dollar
            }
            return R.drawable.ic_launcher_background
        }
    }
}