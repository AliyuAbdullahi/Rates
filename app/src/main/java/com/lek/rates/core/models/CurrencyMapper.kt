package com.lek.rates.core.models

import com.lek.rates.R
import com.lek.rates.core.data.currenciesAndFlags
import com.lek.rates.extensions.isNotSameAs
import com.lek.rates.extensions.isSameAs
import com.lek.rates.extensions.toThreeDecimalPlace
import java.util.Locale

object CurrenciesResponseMapper {
    fun map(currenciesResponse: CurrenciesResponse): List<Currency> =
        currenciesResponse.rates
            ?.let { rates ->
                rates.map { currencyMap ->
                    val currency =
                        currenciesAndFlags()[currencyMap.key.toLowerCase(Locale.getDefault())]
                    Currency(
                        currencyMap.key,
                        currency?.fullName ?: "---",
                        getCurrencyValue(currencyMap, rates).toThreeDecimalPlace(),
                        currency?.flag ?: R.drawable.default_flag
                    ).also { it.id = currencyMap.key.hashCode().toLong() }
                }
            } ?: listOf()

    private fun getCurrencyValue(
        entry: Map.Entry<String, Double>,
        rates: Map<String, Double>
    ): Double {
        val firstResponder = FirstResponder.firstResponder
        val modifierCode = CurrencyModifier.currencyCode
        val originalEntryValue = rates[CurrencyModifier.currencyCode] ?: CurrencyModifier.value
        val multiplier = CurrencyModifier.value / originalEntryValue
        return when {
            CurrencyModifier.isZero() -> 0.0
            CurrencyModifier.isFlaggedEmpty() -> -2.0
            CurrencyModifier.exist().not() -> entry.value
            entry.key isSameAs modifierCode -> {
                CurrencyModifier.value
            }
            entry.key isSameAs firstResponder && firstResponder isNotSameAs modifierCode -> {
                multiplier
            }
            else -> {
                multiplier * entry.value
            }
        }
    }

    private fun getFirstResponder(): Currency {
        val currencyResource =
            currenciesAndFlags()[FirstResponder.firstResponder.toLowerCase(Locale.getDefault())]
        return Currency(
            FirstResponder.firstResponder,
            currencyResource?.fullName ?: "---",
            1.0,
            currencyResource?.flag ?: R.drawable.eur
        )
    }
}
