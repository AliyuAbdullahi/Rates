package com.lek.rates.presentation.ui.rateslistitem

import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.core.models.Currency

class RatesListItemPresenter @ViewModelInject constructor() : BasePresenter<RatesListItemView>() {

    fun bindItem(currency: Currency) {
        view()?.setCurrency(currency.currencyName)
        view()?.setCurrencyCode(currency.currencyCode)
    }
}
