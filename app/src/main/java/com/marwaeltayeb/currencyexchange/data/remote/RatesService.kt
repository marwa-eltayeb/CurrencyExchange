package com.marwaeltayeb.currencyexchange.data.remote

import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesService {

    @GET("latest")
    fun getLatestRates(): Call<RateApiResponse>

    @GET("latest")
    fun getLatestRatesByBase(
        @Query("base") base: String,
    ): Call<RateApiResponse>
}