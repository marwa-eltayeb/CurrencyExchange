package com.marwaeltayeb.currencyexchange.utils

import com.marwaeltayeb.currencyexchange.R

class RateUtils {
    companion object {

        fun getCodeName(key: String): String {
            when (key) {
                "EUR" -> return "Euro"
                "USD" -> return "US Dollar"
                "CAD" -> return "Canadian dollar"
                "HKD" -> return "Hong Kong dollar"
                "ISK" -> return "Icelandic krona"
                "PHP" -> return "Philippine peso"
                "DKK" -> return "Danish krone"
                "HUF" -> return "Hungarian forint"
                "CZK" -> return "Czech koruna"
                "AUD" -> return "Australian Dollar"
                "RON" -> return "Romanian new leu"
                "SEK" -> return "Swedish Krona"
                "IDR" -> return "Indonesian rupiah"
                "INR" -> return "Indian rupee"
                "BRL" -> return "Brazilian real"
                "RUB" -> return "Russian ruble"
                "HRK" -> return "Croatian kuna"
                "JPY" -> return "Japanese yen"
                "THB" -> return "Thai Baht"
                "CHF" -> return "Swiss Franc"
                "SGD" -> return "Singapore Dollar"
                "PLN" -> return "Polish Zloty"
                "BGN" -> return "Bulgarian Lev"
                "TRY" -> return "Turkish Lira"
                "CNY" -> return "Chinese Yuan Renminbi"
                "NOK" -> return "Norwegian Krone"
                "NZD" -> return "New Zealand Dollar"
                "ZAR" -> return "South African Rand"
                "MXN" -> return "Mexican Peso"
                "ILS" -> return "Israeli New Shekel"
                "GBP" -> return "British Pound Sterling"
                "KRW" -> return "South Korean Won"
                "MYR" -> return "Malaysian Ringgit"
                else -> return "None"
            }
        }

        fun getFlag(key: String): Int {
            when (key) {
                "EUR" -> return R.drawable.flag_eur
                "USD" -> return R.drawable.flag_usd
                "CAD" -> return R.drawable.flag_cad
                "HKD" -> return R.drawable.flag_hkd
                "ISK" -> return R.drawable.flag_isk
                "PHP" -> return R.drawable.flag_php
                "DKK" -> return R.drawable.flag_dkk
                "HUF" -> return R.drawable.flag_huf
                "CZK" -> return R.drawable.flag_czk
                "AUD" -> return R.drawable.flag_aud
                "RON" -> return R.drawable.flag_ron
                "SEK" -> return R.drawable.flag_sek
                "IDR" -> return R.drawable.flag_idr
                "INR" -> return R.drawable.flag_inr
                "BRL" -> return R.drawable.flag_brl
                "RUB" -> return R.drawable.flag_rub
                "HRK" -> return R.drawable.flag_hrk
                "JPY" -> return R.drawable.flag_jpy
                "THB" -> return R.drawable.flag_thb
                "CHF" -> return R.drawable.flag_chf
                "SGD" -> return R.drawable.flag_sgd
                "PLN" -> return R.drawable.flag_pln
                "BGN" -> return R.drawable.flag_bgn
                "TRY" -> return R.drawable.flag_try
                "CNY" -> return R.drawable.flag_cny
                "NOK" -> return R.drawable.flag_nok
                "NZD" -> return R.drawable.flag_nzd
                "ZAR" -> return R.drawable.flag_zar
                "MXN" -> return R.drawable.flag_mxn
                "ILS" -> return R.drawable.flag_ils
                "GBP" -> return R.drawable.flag_gbp
                "KRW" -> return R.drawable.flag_krw
                "MYR" -> return R.drawable.flag_myr
                else -> return R.drawable.ic_money
            }
        }
    }
}