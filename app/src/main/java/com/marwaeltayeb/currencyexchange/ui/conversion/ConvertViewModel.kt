package com.marwaeltayeb.currencyexchange.ui.conversion

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse

class ConvertViewModel : ViewModel() {

    private var convertRepository: ConvertRepository? = ConvertRepository()

    fun requestExchangeRate(base: String, symbol: String) {
        return convertRepository!!.requestExchangeRateLiveData(base, symbol)
    }

    fun getExchangeRate(): LiveData<List<Pair<String, Double>>>{
        return convertRepository!!.getExchangeRateLiveData()
    }

    fun requestHistoricalRates(startDate: String, endDate: String ,base: String, symbol: String){
        convertRepository!!.requestHistoricalRates(startDate, endDate,base, symbol)
    }

    fun getHistoricalRates(): LiveData<HistoricApiResponse>{
        return convertRepository!!.getHistoricalRatesLiveData()
    }
}

