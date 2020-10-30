package com.lek.rates.presentation.currencieslist.presenter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.lek.rates.core.base.BasePresenter
import com.lek.rates.presentation.currencieslist.stream.ShowEmptyStateRelay
import com.lek.rates.presentation.currencieslist.stream.ShowLoadingViewRelay
import com.lek.rates.presentation.currencieslist.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class MainPresenter @ViewModelInject constructor(
    private val shouldShowLoadingViewRelay: ShowLoadingViewRelay,
    private val shouldShowEmptyStateRelay: ShowEmptyStateRelay
) : BasePresenter<MainView>() {

    override fun onStart() {
        super.onStart()
        observeShowLoading()
        observeShowEmptyState()
    }

    private fun observeShowLoading() {
        shouldShowLoadingViewRelay.get().observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view()?.showLoading(it)
            }, {
                Log.e("Error", "${it.message}")
            })
    }

    private fun observeShowEmptyState() {
        shouldShowEmptyStateRelay.get().observeOn(AndroidSchedulers.mainThread()).subscribe (
            {
                view()?.showEmptyState(it)
            },
            {
                Log.e("Error", "${it.message}")
            }
        )
    }
}
