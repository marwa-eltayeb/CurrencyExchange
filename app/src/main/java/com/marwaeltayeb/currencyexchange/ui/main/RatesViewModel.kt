package com.marwaeltayeb.currencyexchange.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "RatesViewModel"

class RatesViewModel @Inject constructor(private val ratesRepository: RatesRepository) : ViewModel() {

    private val latestRatesLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()
    private val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private var compositeDisposable = CompositeDisposable()

    fun requestLatestRates(base: String) {
        _dataLoading.value = true
        val observable = ratesRepository.requestLatestRatesLiveData(base)
            .subscribe({ o: RateApiResponse? ->
                Timber.tag(TAG).d( "Succeeded")
                latestRatesLiveData.setValue(o?.rates?.toList())
                _dataLoading.value = false
            }, { e: Throwable -> Timber.tag(TAG).d( "onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getLatestRates(): LiveData<List<Pair<String, Double>>> {
        return latestRatesLiveData
    }

    fun requestExchangeRate(base: String, symbol: String) {
        val observable = ratesRepository.requestExchangeRateLiveData(base, symbol)
            .subscribe({ o: RateApiResponse? ->
                Timber.tag(TAG).d( "Succeeded")
                exchangeRateLiveData.setValue(o?.rates?.toList())
            }, { e: Throwable -> Timber.tag(TAG).d("onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun getExchangeRate(): LiveData<List<Pair<String, Double>>> {
        return exchangeRateLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

