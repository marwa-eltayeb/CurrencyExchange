package com.marwaeltayeb.currencyexchange.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitClient.getRateService().getLatestRates()
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }

                override fun onResponse(
                    call: Call<RateApiResponse>,
                    response: Response<RateApiResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, response.body()!!.rates.toList().get(0).first)
                    }
                }
            })
    }

}