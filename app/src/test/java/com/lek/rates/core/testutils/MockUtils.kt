package com.lek.rates.core.testutils

import com.lek.rates.R
import com.lek.rates.core.models.CurrenciesResponse
import com.lek.rates.core.models.Currency

fun mockCurrenciesResponse() = CurrenciesResponse(
    baseCurrency = "EUR",
    rates = mapOf(
        "NGN" to 500.0,
        "USD" to 1.4
    )
)

fun mockCurrencies() = listOf(
    Currency(
        "USD",
        "US Dollar",
        1.4,
        R.drawable.usd
    ),
    Currency(
        "EUR",
        "Euror",
        1.0,
        R.drawable.eur
    )
)
