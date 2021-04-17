package com.marwaeltayeb.currencyexchange.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertRepository
import javax.inject.Inject

class RatesViewModel@Inject constructor(private val ratesRepository: RatesRepository) : ViewModel() {

    fun requestLatestRates(base: String) {
        return ratesRepository.requestLatestRatesLiveData(base)
    }

    fun getLatestRates(): LiveData<List<Pair<String, Double>>>{
        return ratesRepository.getLatestRatesLiveData()
    }

    fun requestExchangeRate(base: String, symbol: String) {
        return ratesRepository.requestExchangeRateLiveData(base, symbol)
    }

    fun getExchangeRate(): LiveData<List<Pair<String, Double>>>{
        return ratesRepository.getExchangeRateLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        ratesRepository.clearCompositeDisposable()
    }
}

