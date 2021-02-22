package com.marwaeltayeb.currencyexchange.data.model

import com.google.gson.annotations.SerializedName

data class HistoricApiResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("end_at")
    val endAt: String,
    @SerializedName("rates")
    val rates: Map<String, Map<String, Double>>,
    @SerializedName("start_at")
    val startAt: String
)
