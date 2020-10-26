package com.lek.rates.presentation.currencieslist.interactors

import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.presentation.currencieslist.stream.KeyboardOpenedRelay
import com.lek.rates.presentation.currencieslist.stream.ViewScrollingRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3

class GetCanPublishLiveUpdateInteractor(
    private val keyboardOpenedRelay: KeyboardOpenedRelay,
    private val viewScrollingRelay: ViewScrollingRelay,
    private val dataChangedObservable: DataChangedObservable
) {
    operator fun invoke(): Observable<Boolean> {
        return Observable.combineLatest(
            keyboardOpenedRelay.get(),
            viewScrollingRelay.get(),
            dataChangedObservable.getDataChanged(),
            Function3 { keyboardOpened, viewScrolled, dataChanged -> dataChanged && !(viewScrolled || keyboardOpened) }
        )
    }
}
