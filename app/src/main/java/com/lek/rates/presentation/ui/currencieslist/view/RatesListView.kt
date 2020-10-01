package com.lek.rates.presentation.ui.rateslist.view

import com.lek.rates.core.models.Currency

interface RatesListView {
    fun displayRate(currencies: List<Currency>)

    fun showError(message: String)
}
