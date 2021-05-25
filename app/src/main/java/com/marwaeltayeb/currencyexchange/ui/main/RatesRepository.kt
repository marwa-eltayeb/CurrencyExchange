package com.marwaeltayeb.currencyexchange.ui.main

import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RatesService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RatesRepository @Inject constructor(private val ratesService: RatesService) {

    fun requestLatestRatesLiveData(base: String) : Single<RateApiResponse> {
        return ratesService.getLatestRatesByBase(base)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun requestExchangeRateLiveData(base: String, symbol: String):Single<RateApiResponse>{
        return ratesService.getSpecificExchangeRate(base, symbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}