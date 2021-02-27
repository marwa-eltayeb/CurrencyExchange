package com.marwaeltayeb.currencyexchange.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RatesViewModel : ViewModel() {

    private var ratesRepository: RatesRepository? = RatesRepository()

    fun requestLatestRates(base: String) {
        return ratesRepository!!.requestLatestRatesLiveData(base)
    }

    fun getLatestRates(): LiveData<List<Pair<String, Double>>>{
        return ratesRepository!!.getLatestRatesLiveData()
    }

    fun requestExchangeRate(base: String, symbol: String) {
        return ratesRepository!!.requestExchangeRateLiveData(base, symbol)
    }

    fun getExchangeRate(): LiveData<List<Pair<String, Double>>>{
        return ratesRepository!!.getExchangeRateLiveData()
    }
}

