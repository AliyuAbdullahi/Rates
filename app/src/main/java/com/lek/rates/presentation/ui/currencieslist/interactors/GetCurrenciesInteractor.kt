package com.lek.rates.presentation.ui.currencieslist.interactors

import com.lek.rates.core.models.Currency
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.presentation.helper.FirstResponderFactory
import io.reactivex.rxjava3.core.Observable

class GetCurrenciesInteractor(
    private val currenciesRepository: ICurrenciesRepository,
    private val firstResponderFactory: FirstResponderFactory
) {
    operator fun invoke(): Observable<List<Currency>> =
        currenciesRepository.getCurrencies(firstResponderFactory.firstResponder)
}
