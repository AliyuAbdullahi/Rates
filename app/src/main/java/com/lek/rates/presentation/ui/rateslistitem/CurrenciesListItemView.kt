package com.lek.rates.presentation.ui.rateslistitem

import androidx.annotation.DrawableRes

interface CurrenciesListItemView {
    fun setFlagIcon(@DrawableRes flagIcon: Int)
    fun setCurrency(currency: String)
    fun setCurrencyCode(currencyCode: String)
    fun setCurrencyValue(currencyValue: Double)
}
