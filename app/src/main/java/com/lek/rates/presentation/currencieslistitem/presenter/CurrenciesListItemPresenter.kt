package com.lek.rates.presentation.currencieslistitem.presenter

import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.core.models.Currency
import com.lek.rates.presentation.currencieslistitem.view.ListItemView

class CurrenciesListItemPresenter @ViewModelInject constructor() : BasePresenter<ListItemView>() {

    fun bindCurrencyToView(currency: Currency) {
        view()?.setCurrency(currency.currencyName)
        view()?.setCurrencyCode(currency.currencyCode)
        view()?.setCurrencyValue(currency.value)
        view()?.setFlagIcon(currency.flag)
    }
}
