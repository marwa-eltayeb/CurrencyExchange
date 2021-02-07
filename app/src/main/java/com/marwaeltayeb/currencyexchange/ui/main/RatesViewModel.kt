package com.marwaeltayeb.currencyexchange.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RatesViewModel : ViewModel() {

    private var ratesRepository: RatesRepository? = RatesRepository()

    fun getAllRates(): LiveData<List<Pair<String,Double>>> {
        return ratesRepository!!.getMutableLiveData()
    }

    fun getSpecificExchangeRate(base: String, symbol: String): LiveData<List<Pair<String,Double>>> {
        return ratesRepository!!.getMutableLiveData(base, symbol)
    }
}

