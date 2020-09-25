package com.lek.rates.core.models

data class RatesResponse(
    val baseCurrency: String,
    val rates: Rates
)
