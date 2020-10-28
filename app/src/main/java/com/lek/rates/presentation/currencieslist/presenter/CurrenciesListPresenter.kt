package com.lek.rates.presentation.currencieslist.presenter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.core.exceptions.NoNetworkException
import com.lek.rates.core.models.Currency
import com.lek.rates.globals.ErrorMessage
import com.lek.rates.globals.Interval
import com.lek.rates.presentation.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.currencieslist.interactors.GetCurrenciesInteractor
import com.lek.rates.presentation.currencieslist.model.CurrenciesViewData
import com.lek.rates.presentation.currencieslist.stream.NetworkAvailabilityRelay
import com.lek.rates.presentation.currencieslist.stream.ShowEmptyStateRelay
import com.lek.rates.presentation.currencieslist.stream.ShowLoadingViewRelay
import com.lek.rates.presentation.currencieslist.view.ListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CurrenciesListPresenter @ViewModelInject constructor(
    private val getCurrenciesInteractor: GetCurrenciesInteractor,
    private val pollingService: CurrenciesPollingService,
    private val getCanPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor,
    private val shouldShowEmptyStateRelay: ShowEmptyStateRelay,
    private val showLoadingViewRelay: ShowLoadingViewRelay,
    private val networkAvailabilityRelay: NetworkAvailabilityRelay
) : BasePresenter<ListView>() {

    override fun onStart() {
        super.onStart()
        displayListItems()
    }

    override fun onResume() {
        super.onResume()
        pollItems()
    }

    private fun displayListItems() = networkAvailabilityRelay.get()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            if (it) {
                displayRateList()
            } else {
                view()?.showNetworkError(ErrorMessage.noNetworkError)
            }
        }, {
            Log.e("ERROR", "$it")
            handleError(it)
        })

    private fun pollItems() = networkAvailabilityRelay.get()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            if (it) {
                pollCurrencies()
            } else {
                view()?.showNetworkError(ErrorMessage.noNetworkError)
            }
        }, {
            handleError(it)
        })

    private fun displayRateList() {
        addDisposable(
            getCurrenciesInteractor()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLoadingViewRelay.set(false)
                    if (it.isEmpty()) {
                        shouldShowEmptyStateRelay.set(true)
                    } else {
                        view()?.displayRate(it)
                        shouldShowEmptyStateRelay.set(false)
                    }
                }, {
                    shouldShowEmptyStateRelay.set(false)
                    showLoadingViewRelay.set(false)
                    handleError(it)
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
            ).throttleFirst(Interval.MEDIUM, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.liveUpdateEnabled) {
                        view()?.displayRate(it.currencies)
                    }
                }, {
                    handleError(it)
                })
        )
    }

    private fun handleError(it: Throwable) {
        if (it is NoNetworkException) {
            view()?.showNetworkError(it.message ?: ErrorMessage.defaultErrorMessage)
        } else {
            view()?.showError(it.message ?: ErrorMessage.defaultErrorMessage)
        }
    }
}
