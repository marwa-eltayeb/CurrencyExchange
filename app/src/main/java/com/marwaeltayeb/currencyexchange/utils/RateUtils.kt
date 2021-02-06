package com.marwaeltayeb.currencyexchange.utils

import com.marwaeltayeb.currencyexchange.R

class RateUtils {
    companion object {

        fun getCodeName(key: String): String {
            when (key) {
                "BGN" -> return "Marwa"
                "GBP" -> return "Eltayeb"
            }
            return "Nothing"
        }

        fun getFlag(key: String): Int {
            when (key) {
                "BGN" -> return R.drawable.ic_launcher_background
                "GBP" -> return R.drawable.ic_launcher_background
            }
            return R.drawable.ic_launcher_background
        }
    }
}