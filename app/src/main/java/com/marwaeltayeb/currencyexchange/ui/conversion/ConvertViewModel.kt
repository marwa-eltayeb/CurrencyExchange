package com.marwaeltayeb.currencyexchange.ui.conversion

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse

class ConvertViewModel : ViewModel() {

    private var convertRepository: ConvertRepository? = ConvertRepository()

    fun exchangeRate(base: String, symbol: String): LiveData<List<Pair<String, Double>>> {
        return convertRepository!!.getExchangeRateLiveData(base, symbol)
    }

    fun getHistoricalRates(startDate: String, endDate: String ,base: String, symbol: String): LiveData<HistoricApiResponse> {
        return convertRepository!!.getHistoricalRatesLiveData(startDate, endDate,base, symbol)
    }
}

