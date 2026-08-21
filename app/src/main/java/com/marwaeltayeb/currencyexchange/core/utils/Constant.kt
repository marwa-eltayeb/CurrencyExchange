package com.marwaeltayeb.currencyexchange.core.utils

import com.marwaeltayeb.currencyexchange.BuildConfig;

class Constant {
    companion object {
        const val BASE_URL = "https://api.currencybeacon.com/v1/"
        const val FROM_CURRENCY = "USD"
        const val TO_CURRENCY = "EUR"
        const val API_KEY = BuildConfig.API_KEY
    }
}