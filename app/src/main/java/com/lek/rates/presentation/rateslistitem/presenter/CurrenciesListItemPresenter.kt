package com.lek.rates.presentation.rateslistitem.presenter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.core.models.Currency
import com.lek.rates.core.models.ExchangeRateEvaluator
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.rateslistitem.view.ListItemView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ListItemPresenter @ViewModelInject constructor(
    private val getCanPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor
) : BasePresenter<ListItemView>() {

    fun bindCurrencyToView(currency: Currency) {
        view()?.setCurrency(currency.currencyName)
        view()?.setCurrencyCode(currency.currencyCode)
        view()?.setCurrencyValue(currency.value)
        view()?.setFlagIcon(currency.flag)
    }
}
