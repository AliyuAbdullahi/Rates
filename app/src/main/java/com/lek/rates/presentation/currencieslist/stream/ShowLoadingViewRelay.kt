package com.lek.rates.presentation.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable

class ShowLoadingViewRelay {
    private val showLoading = BehaviorRelay.create<Boolean>()

    fun get(): Observable<Boolean> = showLoading

    fun set(shouldShowLoading: Boolean) = showLoading.accept(shouldShowLoading)
}
