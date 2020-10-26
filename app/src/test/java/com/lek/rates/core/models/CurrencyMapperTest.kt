package com.lek.rates.core.models

import com.lek.rates.R
import com.lek.rates.core.testutils.mockCurrenciesResponse
import org.junit.jupiter.api.Test

internal class CurrencyMapperTest {

    @Test
    fun `when CurrenciesResponse is null - CurrenciesResponseMapper returns empty list`() {
        val result = CurrencyMapper.map(CurrenciesResponse().rates)
        assert(result.isEmpty())
    }

    @Test
    fun `when CurrenciesResponse is not null - CurrenciesResponseMapper returns curresponding currencies`() {
        val result = CurrencyMapper.map(mockCurrenciesResponse().rates)
        assert(result.size == 2)
        assert(result[0].currencyName == "Naira")
        assert(result[0].flag == R.drawable.ngn)
        assert(result[1].flag == R.drawable.usd)
        assert(result[1].currencyName == "US Dollar")
        assert(result[0].id == result[0].currencyCode.hashCode().toLong())
    }

    @Test
    fun `when CurrencieResponse is not in local Resource - Default values are assigned`() {
        val currenciesResponse = CurrenciesResponse(
            baseCurrency = "EUR",
            rates = mutableMapOf("DZD" to 0.0066)
        )
        val result = CurrencyMapper.map(currenciesResponse.rates)
        println(result)
        assert(result.size == 1)
        assert(result[0].currencyName == "---")
        assert(result[0].currencyCode == "DZD")
        assert(result[0].value == 0.006)
        assert(result[0].flag == R.drawable.default_flag)
    }
}
