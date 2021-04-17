package com.marwaeltayeb.currencyexchange.data.remote

import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesService {
    @GET("latest")
    fun getLatestRatesByBase(
        @Query("base") base: String,
    ): Single<RateApiResponse>

    @GET("latest")
    fun getSpecificExchangeRate(
        @Query("base") base: String,
        @Query("symbols") symbol: String,
    ): Single<RateApiResponse>

    @GET("history")
    fun getHistoricalRates(
        @Query("start_at") startDate: String,
        @Query("end_at") endDate: String,
        @Query("base") base: String,
        @Query("symbols") symbol: String,
    ): Single<HistoricApiResponse>
}