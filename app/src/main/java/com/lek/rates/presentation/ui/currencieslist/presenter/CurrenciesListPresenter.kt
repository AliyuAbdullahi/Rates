package com.lek.rates.presentation.ui.currencieslist.presenter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.presentation.ui.currencieslist.CurrenciesRelay
import com.lek.rates.presentation.ui.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.ui.currencieslist.interactors.GetCurrenciesInteractor
import com.lek.rates.presentation.ui.currencieslist.view.RatesListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class CurrenciesListPresenter @ViewModelInject constructor(
    private val getCurrenciesInteractor: GetCurrenciesInteractor,
    private val pollingService: CurrenciesPollingService,
    private val currenciesRelay: CurrenciesRelay
) : BasePresenter<RatesListView>() {

    override fun onCreate() {
        super.onCreate()
        displayRateList()
        pollingService()
    }

    override fun onStart() {
        super.onStart()
        addDisposable(
            currenciesRelay.get().observeOn(AndroidSchedulers.mainThread())
                .subscribe({ currencies ->
                    view()?.displayRate(currencies)
                }, {
                    Log.e("ERROR", it.localizedMessage ?: "Unknown")
                    view()?.showError("${it.message}")
                })
        )
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
}
