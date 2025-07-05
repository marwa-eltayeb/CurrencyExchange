package com.marwaeltayeb.currencyexchange.data.model

import com.google.gson.annotations.SerializedName

data class RateApiResponse(
    @SerializedName("date")
    val date: String,
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Map<String, Double>?
)


