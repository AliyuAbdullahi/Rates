package com.lek.rates.presentation.currencieslistitem.view

import androidx.annotation.DrawableRes
import com.jakewharton.rxbinding4.InitialValueObservable
import io.reactivex.rxjava3.core.Observable

interface ListItemView {
    fun setFlagIcon(@DrawableRes flagIcon: Int)
    fun setCurrency(currency: String)
    fun setCurrencyCode(currencyCode: String)
    fun setCurrencyValue(currencyValue: Double)
    fun textChanged(): InitialValueObservable<CharSequence>
    fun isInEditingMode(): Observable<Boolean>
}
