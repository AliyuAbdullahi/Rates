package com.lek.rates.presentation.currencieslist.view

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lek.rates.R
import com.lek.rates.core.models.ExchangeRateEvaluator
import com.lek.rates.core.models.ZERO
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.rateslistitem.presenter.CurrenciesListItemPresenter
import com.lek.rates.presentation.currencieslist.presenter.CurrenciesListPresenter
import com.lek.rates.presentation.currencieslist.stream.KeyboardOpenedRelay
import com.lek.rates.presentation.currencieslist.stream.ViewScrollingRelay
import com.lek.rates.presentation.helper.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_currencies_list.*
import javax.inject.Inject
private const val VIEW_HEIGHT_OFFSET = 100

@AndroidEntryPoint
class CurrenciesListActivity : AppCompatActivity(), OnFlingListener {

    private val presenterCurrencies: CurrenciesListPresenter by viewModels()

    private val currenciesListItemPresenter: CurrenciesListItemPresenter by viewModels()

    @Inject
    lateinit var keyboardOpenedRelay: KeyboardOpenedRelay

    @Inject
    lateinit var viewScrollingRelay: ViewScrollingRelay

    @Inject
    lateinit var canPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies_list)
        presenterCurrencies.attachView(currenciesListView, lifecycle)
        currenciesListView.flingListener = this
        currenciesListView.presenterCurrencies = presenterCurrencies
        currenciesListView.currenciesListItemPresenter = currenciesListItemPresenter
        currenciesListView.lifeCycle = lifecycle
        listContainer.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener)
    }

    private fun keyboardOpened(isOpened: Boolean) {
        keyboardOpenedRelay.set(isOpened)
        Keyboard.isOpened = isOpened
    }

    private var lastHeight = ZERO.toInt()
    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val currentContentHeight = findViewById<View>(Window.ID_ANDROID_CONTENT).height
        if (lastHeight > currentContentHeight + VIEW_HEIGHT_OFFSET) {
            val imputManager: InputMethodManager = listContainer.context
                .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val acceptingText: Boolean = imputManager.isAcceptingText
            if (acceptingText) {
                keyboardOpened(true)
            }
            lastHeight = currentContentHeight
        } else if (currentContentHeight > lastHeight + VIEW_HEIGHT_OFFSET) {
            keyboardOpened(false)
            currenciesListView.clearFocus()
            lastHeight = currentContentHeight
        }
    }

    override fun onFlingStarted() {
        viewScrollingRelay.set(true)
        hideKeyboard { ExchangeRateEvaluator.clear() }
    }

    override fun onFlingStopped() {
        viewScrollingRelay.set(false)
    }
}
