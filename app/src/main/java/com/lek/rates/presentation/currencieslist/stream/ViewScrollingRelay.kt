package com.lek.rates.presentation.ui.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ViewScrollingRelay {
    private val viewScrollingRelay = BehaviorSubject.createDefault(false)

    fun get(): Observable<Boolean> = viewScrollingRelay

    fun set(isScrolling: Boolean) = viewScrollingRelay.onNext(isScrolling)
}
