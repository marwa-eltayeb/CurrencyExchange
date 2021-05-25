package com.marwaeltayeb.currencyexchange.ui.conversion

import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RatesService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ConvertRepository @Inject constructor(private val ratesService: RatesService){

    fun requestExchangeRateLiveData(base: String, symbol: String) : Single<RateApiResponse> {
        return ratesService.getSpecificExchangeRate(base, symbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun requestHistoricalRates(startDate: String, endDate: String ,base: String, symbol: String): Single<HistoricApiResponse> {
        return ratesService.getHistoricalRates(startDate, endDate, base, symbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}