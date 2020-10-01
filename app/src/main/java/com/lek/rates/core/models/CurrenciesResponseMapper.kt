package com.lek.rates.core.models

import com.lek.rates.R
import com.lek.rates.core.data.currencyAndFlags
import java.util.*

object RatesResponseMapper {
    fun map(ratesResponse: RatesResponse): List<Currency> = ratesResponse.rates?.let { rates ->
        rates.map {
            val currency = currencyAndFlags()[it.key.toLowerCase(Locale.getDefault())]
            Currency(
                it.key,
                currency?.fullName ?: "-",
                it.value,
                currency?.flag ?: R.drawable.default_flag
            )
        }
    } ?: listOf()
}
