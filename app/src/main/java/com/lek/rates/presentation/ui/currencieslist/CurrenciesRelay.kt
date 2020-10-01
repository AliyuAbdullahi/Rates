package com.lek.rates.presentation.ui.currencieslist

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import com.lek.rates.core.models.Currency

class CurrenciesRelay {
    private val currenciesSubject = BehaviorSubject.create<List<Currency>>()

    fun get(): Observable<List<Currency>> = currenciesSubject

    fun setCurrencies(currencies: List<Currency>) = currenciesSubject.onNext(currencies)
}
