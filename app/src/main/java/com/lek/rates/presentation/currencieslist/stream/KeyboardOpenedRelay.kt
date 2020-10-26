package com.lek.rates.presentation.ui.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class KeyboardOpenedRelay {
    private val keyBoardOpenedRelay = BehaviorRelay.createDefault(false)

    fun get(): Observable<Boolean> = keyBoardOpenedRelay

    fun set(keyboardOpened: Boolean) = keyBoardOpenedRelay.accept(keyboardOpened)
}
