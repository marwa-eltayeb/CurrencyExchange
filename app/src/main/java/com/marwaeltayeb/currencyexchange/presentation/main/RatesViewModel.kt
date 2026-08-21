package com.marwaeltayeb.currencyexchange.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.data.repositories.CurrencyRepository
import com.marwaeltayeb.currencyexchange.data.repositories.RatesRepository
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "RatesViewModel"

class RatesViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _latestRatesLiveData = MutableLiveData<List<Pair<String, Double>>>()
    val latestRatesLiveData: MutableLiveData<List<Pair<String, Double>>> = _latestRatesLiveData

    private val _exchangeRateLiveData = MutableLiveData<List<Pair<String, Double>>>()
    val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = _exchangeRateLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var fullRatesList: List<Pair<String, Double>> = emptyList()
    // Job for managing search debounce
    private var searchJob: Disposable? = null

    private var compositeDisposable = CompositeDisposable()

    fun requestLatestRates(base: String) {
        _isLoading.value = true
        val observable = ratesRepository.requestLatestRates(base)
            .subscribe({ rateApiResponse: RateApiResponse ->
                Timber.tag(TAG).d("onSuccess: $rateApiResponse")
                rateApiResponse.rates?.let {
                    fullRatesList = it.toList()
                    _latestRatesLiveData.value = fullRatesList
                }
                _isLoading.value = false
            }, { e: Throwable ->
                Timber.tag(TAG).d("onFailure: ${e.message}")
                _isLoading.value = false
                _errorMessage.value = "Failed to load latest rates: ${e.message}"
            })
        compositeDisposable.add(observable)
    }

    fun requestExchangeRate(base: String, symbol: String) {
        val observable = ratesRepository.requestExchangeRate(base, symbol)
            .subscribe({ rateApiResponse: RateApiResponse ->
                Timber.tag(TAG).d("onSuccess: $rateApiResponse")
                rateApiResponse.rates?.let { _exchangeRateLiveData.value = it.toList() }
            }, { e: Throwable ->
                Timber.tag(TAG).d("onFailure: ${e.message}")
            })
        compositeDisposable.add(observable)
    }


    fun filterCurrencies(query: String) {
        searchJob?.dispose() // Cancel previous search job if active
        searchJob = Observable.just(query)
            .debounce(300, TimeUnit.MILLISECONDS) // Debounce for 300ms
            .observeOn(Schedulers.computation())
            .map { filterQuery ->
                if (filterQuery.isBlank()) {
                    fullRatesList // Return full list if query is empty
                } else {
                    fullRatesList.filter { pair ->
                        val currencyCode = pair.first
                        val currencyName = currencyRepository.getCurrencyName(currencyCode)
                        currencyCode.contains(filterQuery, ignoreCase = true) || currencyName?.contains(filterQuery, ignoreCase = true) == true
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ filteredList ->
                _latestRatesLiveData.value = filteredList
            }, { throwable ->
                Timber.tag(TAG).e("Error during filtering: ${throwable.message}")
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        searchJob?.dispose()
    }
}

