package com.lek.rates.core.repository

import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.core.data.CurrenciesCache
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.data.FetchedRates
import com.lek.rates.core.models.CurrenciesResponse
import com.lek.rates.core.models.Currency
import com.lek.rates.core.models.FirstResponder
import io.reactivex.rxjava3.core.Observable

class CurrenciesRepository(
    private val currenciesService: CurrenciesService,
    private val dataChangedObservable: DataChangedObservable,
    private val fetchedRate: FetchedRates
) : ICurrenciesRepository {

    override fun getCurrencies(baseCurrency: String): Observable<List<Currency>> =
        getCurrenciesFromServerAndAddFirstResponder(baseCurrency)

    private fun getCurrenciesFromServerAndAddFirstResponder(baseCurrency: String): Observable<List<Currency>> {
        return currenciesService.getLatestCurrencyRates(baseCurrency)
            .flatMap {
                val newMap = mutableMapOf<String, Double>()
                newMap[FirstResponder.firstResponder] = FirstResponder.value
                it.rates?.let { rates -> newMap.putAll(rates) }
                it.rates?.clear()
                it.rates?.putAll(newMap)
                checkIfDataIsSameAsFetched(it)
                Observable.just(CurrenciesCache.setCurrenciesFromMap(newMap))
            }
    }

    private fun checkIfDataIsSameAsFetched(it: CurrenciesResponse) {
        dataChangedObservable.setDataChanged(fetchedRate.get() != it)
        fetchedRate.set(it)
    }
}
