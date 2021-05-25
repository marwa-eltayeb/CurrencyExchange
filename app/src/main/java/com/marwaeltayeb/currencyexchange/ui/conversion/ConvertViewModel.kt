package com.marwaeltayeb.currencyexchange.ui.conversion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ConvertViewModel"

class ConvertViewModel @Inject constructor(private val convertRepository: ConvertRepository) : ViewModel() {

    private val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()
    private val historicalRatesLiveData: MutableLiveData<HistoricApiResponse> = MutableLiveData<HistoricApiResponse>()
    private var compositeDisposable = CompositeDisposable()

    fun requestExchangeRate(base: String, symbol: String) {
        val observable = convertRepository.requestExchangeRateLiveData(base, symbol)
            .subscribe({ o: RateApiResponse? ->
                Log.d(TAG, "Succeeded")
                exchangeRateLiveData.setValue(o?.rates?.toList())
            }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getExchangeRate(): LiveData<List<Pair<String, Double>>> {
        return exchangeRateLiveData
    }

    fun requestHistoricalRates(startDate: String, endDate: String, base: String, symbol: String) {
        val observable = convertRepository.requestHistoricalRates(startDate, endDate, base, symbol)
            .subscribe({ o: HistoricApiResponse? ->
                Log.d(TAG, "Succeeded")
                historicalRatesLiveData.setValue(o)
            }, { e: Throwable -> Log.d(TAG, "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getHistoricalRates(): LiveData<HistoricApiResponse> {
        return historicalRatesLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

