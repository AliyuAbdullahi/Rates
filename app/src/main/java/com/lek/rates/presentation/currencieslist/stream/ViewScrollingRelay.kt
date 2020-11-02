package com.lek.rates.presentation.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable

class ViewScrollingRelay {
    private val viewScrollingRelay = BehaviorRelay.createDefault(false)

    fun get(): Observable<Boolean> = viewScrollingRelay

    fun set(isScrolling: Boolean) = viewScrollingRelay.accept(isScrolling)
}
