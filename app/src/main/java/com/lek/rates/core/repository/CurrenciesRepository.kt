package com.lek.rates.core.repository

import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.data.FetchedRates
import com.lek.rates.core.models.Currency
import com.lek.rates.core.models.CurrenciesResponse
import com.lek.rates.core.models.CurrenciesResponseMapper
import io.reactivex.rxjava3.core.Observable

class RatesRepository(
    private val currenciesService: CurrenciesService,
    private val dataChangedObservable: DataChangedObservable,
    private val fetchedRate: FetchedRates
) : IRatesRepository {

    override fun getRates(baseCurrency: String): Observable<List<Currency>> {
        return currenciesService.getLatestCurrencyRates(baseCurrency)
            .flatMap {
                checkIfDataIsSameAsFetched(it)
                Observable.just(CurrenciesResponseMapper.map(it))
            }
    }

    private fun checkIfDataIsSameAsFetched(it: CurrenciesResponse) {
        dataChangedObservable.setDataChanged(fetchedRate.rates != it)
        fetchedRate.rates = it
    }
}
