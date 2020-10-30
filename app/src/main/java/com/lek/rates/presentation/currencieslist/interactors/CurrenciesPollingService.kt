package com.lek.rates.presentation.currencieslist.interactors

import com.lek.rates.core.models.Currency
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.models.FirstResponder
import com.lek.rates.globals.ONE
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class CurrenciesPollingService(
    private val currenciesRepository: ICurrenciesRepository,
    private val firstResponderFactory: FirstResponder
) {
    operator fun invoke(): Observable<List<Currency>> =
        Observable.interval(ONE.toLong(), TimeUnit.SECONDS).flatMap {
            currenciesRepository.getCurrencies(firstResponderFactory.firstResponder)
        }
}
