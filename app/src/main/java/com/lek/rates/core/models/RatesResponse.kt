package com.lek.rates.core.models

import com.google.gson.annotations.SerializedName

data class RatesResponse(
    @SerializedName("baseCurrency")
    val baseCurrency: String? = null,
    @SerializedName("rates")
    val rates: Map<String, Double>? = null
)