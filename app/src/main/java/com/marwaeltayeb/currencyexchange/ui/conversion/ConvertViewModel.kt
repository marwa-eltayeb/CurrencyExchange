package com.marwaeltayeb.currencyexchange.ui.conversion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.data.ConvertRepository
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.RatesRepository
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "ConvertViewModel"

class ConvertViewModel @Inject constructor(private val convertRepository: ConvertRepository, private val ratesRepository: RatesRepository) : ViewModel() {

    private val _exchangeRateLiveData = MutableLiveData<List<Pair<String, Double>>>()
    val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = _exchangeRateLiveData

    private val _historicalRatesLiveData = MutableLiveData<Map<String, Map<String, Double>>>()
    val historicalRatesLiveData: MutableLiveData<Map<String, Map<String, Double>>> = _historicalRatesLiveData

    private var compositeDisposable = CompositeDisposable()

    fun requestExchangeRate(base: String, symbol: String) {
        val observable = ratesRepository.requestExchangeRate(base, symbol)
            .subscribe({ rateApiResponse: RateApiResponse ->
                Timber.tag(TAG).d("onSuccess")
                rateApiResponse.rates?.let { _exchangeRateLiveData.value = it.toList() }
            }, { e: Throwable -> Timber.tag(TAG).d("onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    fun requestHistoricalRates(startDate: String, endDate: String, base: String, symbol: String) {
        val observable = convertRepository.requestHistoricalRates(startDate, endDate, base, symbol)
            .subscribe({ historicApiResponse: HistoricApiResponse ->
                Timber.tag(TAG).d("onSuccess: $historicApiResponse")
                historicApiResponse.rates?.let { _historicalRatesLiveData.value = it }
            }, { e: Throwable -> Timber.tag(TAG).d("onFailure: ${e.message.toString()}") })

        compositeDisposable.add(observable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

