package com.marwaeltayeb.currencyexchange.data.model


import com.google.gson.annotations.SerializedName
import java.util.*

data class RateApiResponse(
    @SerializedName("rates")
    val rates: Hashtable<String, Double>,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String
)