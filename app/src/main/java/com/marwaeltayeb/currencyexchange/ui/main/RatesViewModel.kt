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

    private val _latestRatesLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()
    val latestRatesLiveData: MutableLiveData<List<Pair<String, Double>>> = _latestRatesLiveData

    private val _exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String, Double>>>()
    val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = _exchangeRateLiveData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private var compositeDisposable = CompositeDisposable()

    fun requestLatestRates(base: String) {
        _dataLoading.value = false
        val observable = ratesRepository.requestLatestRates(base)
            .subscribe({ rateApiResponse: RateApiResponse ->
                Timber.tag(TAG).d("Succeeded")
                _latestRatesLiveData.value = rateApiResponse.rates.toList()
                _dataLoading.value = true
            }, { e: Throwable -> Timber.tag(TAG).d("onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun requestExchangeRate(base: String, symbol: String) {
        val observable = ratesRepository.requestExchangeRate(base, symbol)
            .subscribe({ rateApiResponse: RateApiResponse ->
                Timber.tag(TAG).d("Succeeded")
                _exchangeRateLiveData.setValue(rateApiResponse.rates.toList())
            }, { e: Throwable -> Timber.tag(TAG).d("onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

