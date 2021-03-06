package com.lek.rates.presentation.currencieslist.view

import com.lek.rates.core.models.Currency
import com.lek.rates.presentation.currencieslistitem.view.CurrenciesListItemView

interface ListView {
    fun displayRate(currencies: List<Currency>)
    fun showError(message: String)
    fun bindItemEdited(view: CurrenciesListItemView, currency: Currency)
    fun showNetworkError(errorMessage: String)
}
