package com.lek.rates.presentation.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ShowEmptyStateRelay {
    private val showEmptyState = BehaviorRelay.createDefault(false)

    fun get(): Observable<Boolean> = showEmptyState

    fun set(shouldShowEmptyState: Boolean) = showEmptyState.accept(shouldShowEmptyState)
}
