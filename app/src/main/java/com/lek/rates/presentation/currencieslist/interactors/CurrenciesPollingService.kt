package com.lek.rates.presentation.ui.currencieslist.interactors

import com.lek.rates.core.models.Currency
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.models.FirstResponder
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class CurrenciesPollingService(
    private val currenciesRepository: ICurrenciesRepository,
    private val firstResponderFactory: FirstResponder
) {
    operator fun invoke(): Observable<List<Currency>> =
        Observable.interval(1, TimeUnit.SECONDS).flatMap {
            currenciesRepository.getCurrencies(firstResponderFactory.firstResponder)
        }
}
