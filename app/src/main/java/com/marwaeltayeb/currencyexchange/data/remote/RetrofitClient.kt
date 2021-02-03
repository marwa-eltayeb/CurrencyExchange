package com.marwaeltayeb.currencyexchange.data.remote

import com.marwaeltayeb.currencyexchange.utils.Const
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitClient {
    private val retrofit: Retrofit
    private lateinit var retrofitClient: RetrofitClient

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRateService(): RatesService {
        return retrofit.create(RatesService::class.java)
    }
}