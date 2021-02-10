package com.marwaeltayeb.currencyexchange.ui.conversion

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ConvertViewModel : ViewModel() {

    private var convertRepository: ConvertRepository? = ConvertRepository()

    fun exchangeRate(base: String, symbol: String): LiveData<List<Pair<String, Double>>> {
        return convertRepository!!.getMutableLiveData(base, symbol)
    }

}

