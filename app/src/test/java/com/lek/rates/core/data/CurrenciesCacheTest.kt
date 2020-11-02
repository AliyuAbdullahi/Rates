package com.lek.rates.core.data

import com.lek.rates.BaseTest
import com.lek.rates.R
import com.lek.rates.core.models.Currency
import org.junit.jupiter.api.Test

internal class CurrenciesCacheTest : BaseTest() {

    @Test
    fun `when CurrenciesCache is updated with currencies - it is cached for reuse`() {
        CurrenciesCache.clear()
        val currencies = currencies()
        CurrenciesCache.setCurrenciesFromMap(currencies)
        val availableCurrencies = CurrenciesCache.get()
        val cachedMap = CurrenciesCache.getCache()
        println(cachedMap.size)
        println(availableCurrencies.size)
        assert(availableCurrencies.size == 3)
        assert(cachedMap.size == 3)
        assert(availableCurrencies[0].currencyCode == "NGN")
        assert(cachedMap["NGN"]!!.value == 500.0)
    }

    @Test
    fun `when FirstResponder is updated - cache is updated with the new FirstResponder`() {
        CurrenciesCache.clear()
        val currencies = currencies()
        CurrenciesCache.setCurrenciesFromMap(currencies)
        CurrenciesCache.setAsFirstResponder(Currency("USD", "US Dollar", 1.4, R.drawable.usd))
        assert(CurrenciesCache.get()[0].currencyCode == "USD")
    }

    private fun currencies() = mutableMapOf(
        "NGN" to 500.0,
        "USD" to 1.4,
        "EUR" to 1.0
    )
}
