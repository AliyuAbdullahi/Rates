package com.lek.rates.presentation.currencieslist.presenter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.core.models.Currency
import com.lek.rates.presentation.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.currencieslist.interactors.GetCurrenciesInteractor
import com.lek.rates.presentation.currencieslist.model.CurrenciesViewData
import com.lek.rates.presentation.currencieslist.stream.ViewScrollingRelay
import com.lek.rates.presentation.rateslistitem.view.CurrenciesListItemView
import com.lek.rates.presentation.currencieslist.view.ListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ListPresenter @ViewModelInject constructor(
    private val getCurrenciesInteractor: GetCurrenciesInteractor,
    private val pollingService: CurrenciesPollingService,
    private val getCanPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor,
    private val viewScrollingRelay: ViewScrollingRelay
) : BasePresenter<ListView>() {

    override fun onCreate() {
        super.onCreate()
        displayRateList()
        pollCurrencies()
    }

    private fun displayRateList() {
        addDisposable(
            getCurrenciesInteractor()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view()?.displayRate(it)
                }, {
                    Log.e("ERROR", it.localizedMessage ?: "Unknown")
                    view()?.showError("${it.message}")
                })
        )
    }

    private fun pollCurrencies() {
        addDisposable(
            Observable.combineLatest(
                getCanPublishLiveUpdateInteractor(),
                pollingService(),
                BiFunction { liveUpdateEnabled: Boolean, currencies: List<Currency> ->
                    CurrenciesViewData(liveUpdateEnabled, currencies)
                }
            ).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.liveUpdateEnabled) {
                        view()?.displayRate(it.currencies)
                    }
                }, {
                    Log.e("Error", "Error polling frequencies $it")
                })
        )
    }
}
