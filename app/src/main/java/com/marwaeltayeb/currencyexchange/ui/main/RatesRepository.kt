package com.marwaeltayeb.currencyexchange.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "RatesRepository"

class RatesRepository {

    private val latestRatesLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()
    private val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()

    var compositeDisposable = CompositeDisposable()

    fun requestLatestRatesLiveData(base: String) {

        val observable =
            RetrofitClient.getRateService().getLatestRatesByBase(base)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ o: RateApiResponse? ->
                    Log.d(TAG, "Succeeded")
                    latestRatesLiveData.setValue(o?.rates?.toList()) }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getLatestRatesLiveData(): MutableLiveData<List<Pair<String, Double>>> {
        return latestRatesLiveData
    }

    fun requestExchangeRateLiveData(base: String, symbol: String) {

        val observable =
            RetrofitClient.getRateService().getSpecificExchangeRate(base, symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ o: RateApiResponse? ->
                    Log.d(TAG, "Succeeded")
                    exchangeRateLiveData.setValue(o?.rates?.toList()) }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getExchangeRateLiveData(): MutableLiveData<List<Pair<String, Double>>>{
        return exchangeRateLiveData
    }

    fun clearCompositeDisposable(){
        compositeDisposable.clear()
    }
}