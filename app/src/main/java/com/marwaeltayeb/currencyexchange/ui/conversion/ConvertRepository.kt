package com.marwaeltayeb.currencyexchange.ui.conversion

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RatesService
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "ConvertRepository"

class ConvertRepository @Inject constructor(private val ratesService: RatesService){

    private val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String,Double>>>()
    private val historicalRatesLiveData: MutableLiveData<HistoricApiResponse> = MutableLiveData<HistoricApiResponse>()

    var compositeDisposable = CompositeDisposable()

    fun requestExchangeRateLiveData(base: String, symbol: String){

        val observable =
            ratesService.getSpecificExchangeRate(base, symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ o: RateApiResponse? ->
                    Log.d(TAG, "Succeeded")
                    exchangeRateLiveData.setValue(o?.rates?.toList()) }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getExchangeRateLiveData(): MutableLiveData<List<Pair<String,Double>>>{
        return exchangeRateLiveData
    }

    fun requestHistoricalRates(startDate: String, endDate: String ,base: String, symbol: String) {

        val observable =
            ratesService.getHistoricalRates(startDate, endDate, base, symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ o: HistoricApiResponse? ->
                    Log.d(TAG, "Succeeded")
                    historicalRatesLiveData.setValue(o) }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getHistoricalRatesLiveData():MutableLiveData<HistoricApiResponse> {
        return historicalRatesLiveData
    }

    fun clearCompositeDisposable(){
        compositeDisposable.clear()
    }
}