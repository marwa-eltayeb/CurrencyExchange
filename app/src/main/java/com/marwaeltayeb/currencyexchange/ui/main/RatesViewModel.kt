package com.marwaeltayeb.currencyexchange.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RatesViewModel : ViewModel() {

    private var ratesRepository: RatesRepository? = RatesRepository()

    fun getAllRates(base: String): LiveData<List<Pair<String,Double>>> {
        return ratesRepository!!.getLatestRatesLiveData(base)
    }

    fun getSpecificExchangeRate(base: String, symbol: String): LiveData<List<Pair<String,Double>>> {
        return ratesRepository!!.getExchangeRateLiveData(base, symbol)
    }
}

