package com.lek.rates.presentation.ui.currencieslist

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CanPublishCurrenciesRelay {
    private val canPublishCurrenciesSubject = BehaviorSubject.create<Boolean>()

    fun get(): Observable<Boolean> = canPublishCurrenciesSubject

    fun set(canPublish: Boolean) = canPublishCurrenciesSubject.onNext(canPublish)
}
