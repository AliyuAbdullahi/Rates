package com.lek.rates.core.data

import com.lek.rates.core.models.Currency
import com.lek.rates.core.models.CurrencyMapper
import com.lek.rates.core.models.ExchangeRateEvaluator
import com.lek.rates.core.models.FirstResponder
import com.lek.rates.extensions.isNotSameAs
import com.lek.rates.extensions.isSameAs
import com.lek.rates.extensions.toThreeDecimalPlace

object CurrenciesCache {

    private var currencies: MutableList<Currency> = mutableListOf()

    private val cache = mutableMapOf<String, Currency>()

    private val newItems: MutableList<Currency> = mutableListOf()

    fun setCurrenciesFromMap(map: Map<String, Double>): List<Currency> {
        newItems.clear()
        val oldCache = cache
        val currencyMapResult = CurrencyMapper.map(map)
        currencyMapResult.forEach { cache[it.currencyCode] = it }

        if (currencies.isEmpty()) {
            // load data first time
            currencies = cache.values.toMutableList()
        } else {
            //attempt update existing values
            currencies.forEach { currency ->
                if (cache.keys.contains(currency.currencyCode)) {
                    cache[currency.currencyCode]?.let {
                        currency.value = it.value
                    }
                }
            }

            // Add new values if any
            cache.forEach {
                if (oldCache.containsKey(it.key).not()) {
                    currencies.add(it.value)
                    newItems.add(it.value)
                }
            }
        }

        return currencies.toList()
    }

    fun get() = currencies

    fun getCache() = cache

    fun updateCurrencies(currencies: List<Currency>) {
        this.currencies = currencies.toMutableList()
    }

    fun newData() = newItems
}
