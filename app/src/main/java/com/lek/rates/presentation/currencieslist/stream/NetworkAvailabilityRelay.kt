package com.lek.rates.presentation.currencieslist.stream

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable

class NetworkAvailabilityRelay {
    private val isNetworkAvailable = BehaviorRelay.createDefault(false)

    fun get(): Observable<Boolean> = isNetworkAvailable

    fun set(networkAvailable: Boolean) = isNetworkAvailable.accept(networkAvailable)
}
