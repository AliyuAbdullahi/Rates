package com.lek.rates.presentation.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable

class KeyboardOpenedRelay {
    private val keyBoardOpenedRelay = BehaviorRelay.createDefault(false)

    fun get(): Observable<Boolean> = keyBoardOpenedRelay

    fun set(keyboardOpened: Boolean) = keyBoardOpenedRelay.accept(keyboardOpened)
}
