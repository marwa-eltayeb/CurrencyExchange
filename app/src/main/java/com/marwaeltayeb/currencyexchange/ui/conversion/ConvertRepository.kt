package com.marwaeltayeb.currencyexchange.ui.conversion

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConvertRepository {

    private val exchangeRateLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String,Double>>>()
    private val historicalRatesLiveData: MutableLiveData<HistoricApiResponse> = MutableLiveData<HistoricApiResponse>()

    fun requestExchangeRateLiveData(base: String, symbol: String){
        RetrofitClient.getRateService().getSpecificExchangeRate(base, symbol)
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<RateApiResponse>, response: Response<RateApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            exchangeRateLiveData.setValue(response.body()!!.rates.toList())
                        }
                    }
                }
            })
    }

    fun getExchangeRateLiveData(): MutableLiveData<List<Pair<String,Double>>>{
        return exchangeRateLiveData
    }

    fun requestHistoricalRates(startDate: String, endDate: String ,base: String, symbol: String) {
        RetrofitClient.getRateService().getHistoricalRates(startDate, endDate, base, symbol)
            .enqueue(object : Callback<HistoricApiResponse> {
                override fun onFailure(call: Call<HistoricApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<HistoricApiResponse>, response: Response<HistoricApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            historicalRatesLiveData.setValue(response.body())
                        }
                    }
                }
            })
    }

    fun getHistoricalRatesLiveData():MutableLiveData<HistoricApiResponse> {
        return historicalRatesLiveData
    }
}