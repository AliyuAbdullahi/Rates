package com.lek.rates.presentation.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ShowLoadingViewRelay {
    private val showLoading = BehaviorRelay.create<Boolean>()

    fun get(): Observable<Boolean> = showLoading

    fun set(shouldShowLoading: Boolean) = showLoading.accept(shouldShowLoading)
}
