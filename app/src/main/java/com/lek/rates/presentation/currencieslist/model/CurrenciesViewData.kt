package com.lek.rates.presentation.ui.currencieslist.model

import com.lek.rates.core.models.Currency

data class CurrenciesViewData(val liveUpdateEnabled: Boolean, val currencies: List<Currency>)
