package com.lek.rates.core.models

import com.lek.rates.R
import com.lek.rates.core.data.currencyAndFlags
import java.util.*

object CurrenciesResponseMapper {
    fun map(currenciesResponse: CurrenciesResponse): List<Currency> =
        currenciesResponse.rates?.let { rates ->
            rates.map { currencyMap ->
                val currency = currencyAndFlags()[currencyMap.key.toLowerCase(Locale.getDefault())]
                Currency(
                    currencyMap.key,
                    currency?.fullName ?: "---",
                    currencyMap.value,
                    currency?.flag ?: R.drawable.default_flag
                ).also { it.id = currencyMap.key.hashCode().toLong() }
            }
        } ?: listOf()
}
