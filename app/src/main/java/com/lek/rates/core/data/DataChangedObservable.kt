package com.lek.rates.core.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DataChangedObservable {

    private val dataChangedSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun setDataChanged(hasChanged: Boolean) = dataChangedSubject.onNext(hasChanged)

    fun getDataChanged(): Observable<Boolean> = dataChangedSubject
}
