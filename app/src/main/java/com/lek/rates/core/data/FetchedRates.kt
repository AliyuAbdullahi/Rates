package com.lek.rates.core.data

import com.lek.rates.core.models.CurrenciesResponse

class FetchedRates {
    private var rates = CurrenciesResponse()

    fun get() = rates

    fun set(currencyResponse: CurrenciesResponse) {
        rates = currencyResponse
    }
}
