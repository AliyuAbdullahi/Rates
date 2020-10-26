package com.lek.rates.core.models

import com.lek.rates.R
import com.lek.rates.core.data.currenciesAndFlags
import com.lek.rates.extensions.isNotSameAs
import com.lek.rates.extensions.isSameAs
import com.lek.rates.extensions.toThreeDecimalPlace
import java.util.Locale

object CurrencyMapper {
    fun map(currencyRates: Map<String, Double>?): List<Currency> =
        currencyRates
            ?.let { rates ->
                rates.map { currencyMap ->
                    val currency = currenciesAndFlags()[currencyMap.key.toLowerCase(Locale.getDefault())]
                    Currency(
                        currencyMap.key,
                        currency?.fullName ?: "---",
                        getCurrencyValue(currencyMap, rates).toThreeDecimalPlace(),
                        currency?.flag ?: R.drawable.default_flag
                    ).also { it.id = currencyMap.key.hashCode().toLong() }
                }
            } ?: listOf()

    private fun getCurrencyValue(
        currentEntry: Map.Entry<String, Double>,
        allEntries: Map<String, Double>
    ): Double {
        val firstResponder = FirstResponder.firstResponder
        val modifierCode = ExchangeRateEvaluator.currencyCode
        val selectedExchangeRate = allEntries[ExchangeRateEvaluator.currencyCode] ?: ExchangeRateEvaluator.value
        val multiplier = selectedExchangeRate / ExchangeRateEvaluator.value
        return when {
            ExchangeRateEvaluator.hasNoValue() -> currentEntry.value
            ExchangeRateEvaluator.isBlank() -> ZERO
            currentEntry.key isSameAs modifierCode -> {
                ExchangeRateEvaluator.value
            }
            currentEntry.key isSameAs firstResponder && firstResponder isNotSameAs modifierCode -> {
                multiplier
            }
            else -> {
                multiplier * currentEntry.value
            }
        }
    }
}
