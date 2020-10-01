package com.lek.rates.presentation.ui.currencieslist.interactors

import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.models.Currency
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.presentation.helper.FirstResponderFactory
import com.lek.rates.presentation.ui.currencieslist.CanPublishCurrenciesRelay
import com.lek.rates.presentation.ui.currencieslist.CurrenciesRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import java.util.concurrent.TimeUnit

class CurrenciesPollingService(
    private val currenciesRepository: ICurrenciesRepository,
    private val firstResponderFactory: FirstResponderFactory,
    private val canPublishCurrenciesRelay: CanPublishCurrenciesRelay,
    private val currenciesRelay: CurrenciesRelay,
    private val dataChangedObservable: DataChangedObservable
) {
    operator fun invoke(): Observable<Unit> =
        Observable.interval(1, TimeUnit.SECONDS).flatMap {
            Observable.combineLatest(
                currenciesRepository.getCurrencies(firstResponderFactory.firstResponder),
                canPublishCurrenciesRelay.get(),
                dataChangedObservable.getDataChanged(),
                Function3 { currencies: List<Currency>, canPublish: Boolean, dataChanged: Boolean ->
                    if (canPublish && dataChanged) {
                        currenciesRelay.setCurrencies(currencies)
                    }
                }
            )
        }
}
