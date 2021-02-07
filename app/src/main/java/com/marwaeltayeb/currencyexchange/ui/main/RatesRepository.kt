package com.marwaeltayeb.currencyexchange.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatesRepository {

    private var ratesList: List<Pair<String,Double>> = ArrayList<Pair<String,Double>>()
    private val mutableLiveData: MutableLiveData<List<Pair<String,Double>>> = MutableLiveData<List<Pair<String,Double>>>()
    private val mutableLiveDataForRate: MutableLiveData<List<Pair<String,Double>>> = MutableLiveData<List<Pair<String,Double>>>()

    fun getMutableLiveData(): MutableLiveData<List<Pair<String,Double>>> {
        RetrofitClient.getRateService().getLatestRates()
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<RateApiResponse>, response: Response<RateApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            ratesList = response.body()!!.rates.toList()
                            mutableLiveData.setValue(ratesList)
                        }
                    }
                }
            })
        return mutableLiveData
    }

    fun getMutableLiveData(base: String, symbol: String): MutableLiveData<List<Pair<String,Double>>> {
        RetrofitClient.getRateService().getSpecificExchangeRate(base, symbol)
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<RateApiResponse>, response: Response<RateApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            mutableLiveDataForRate.setValue(response.body()!!.rates.toList())
                        }
                    }
                }
            })
        return mutableLiveDataForRate
    }
}