package com.marwaeltayeb.currencyexchange.data.model

import com.google.gson.annotations.SerializedName

data class HistoricApiResponse(
    @SerializedName("response")
    val rates: Map<String, Map<String, Double>>?
)
