package com.lek.rates.presentation.currencieslist.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import com.lek.rates.R
import com.lek.rates.core.data.CurrenciesCache
import com.lek.rates.core.models.Currency
import com.lek.rates.core.models.ExchangeRateEvaluator
import com.lek.rates.core.models.FirstResponder
import com.lek.rates.extensions.isNotSameAs
import com.lek.rates.extensions.toThreeDecimalPlace
import com.lek.rates.globals.EMPTY_STRING
import com.lek.rates.globals.ErrorMessage
import com.lek.rates.globals.Interval
import com.lek.rates.globals.ZERO
import com.lek.rates.logger.Logger
import com.lek.rates.presentation.currencieslist.presenter.CurrenciesListPresenter
import com.lek.rates.presentation.currencieslistitem.presenter.CurrenciesListItemPresenter
import com.lek.rates.presentation.currencieslistitem.view.CurrenciesListItemView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

private const val DELAY_MILLIS = 100L

class CurrenciesListView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = ZERO.toInt()
) : ScrollView(context, attributeSet, defStyle), ListView {

    private var items = LinkedList<Currency>()

    lateinit var flingListener: OnFlingListener

    private var mScrollChecker: Runnable? = null

    private var mPreviousPosition = ZERO.toInt()

    lateinit var presenterCurrencies: CurrenciesListPresenter

    lateinit var currenciesListItemPresenter: CurrenciesListItemPresenter

    lateinit var lifeCycle: Lifecycle

    private val disposable = CompositeDisposable()

    init {
        inflate(context, R.layout.currencies_list_view, this)
        mScrollChecker = Runnable {
            val position = scrollY
            if (mPreviousPosition - position == 0) {
                flingListener.onFlingStopped()
                removeCallbacks(mScrollChecker)
            } else {
                mPreviousPosition = scrollY
                postDelayed(mScrollChecker, DELAY_MILLIS)
            }
        }
    }

    private fun setFirstResponder(currency: Currency) {
        items.remove(currency)
        items.addFirst(currency)
        CurrenciesCache.setAsFirstResponder(currency)
        val containter = findViewById<LinearLayout>(R.id.currenciesListContainer)
        containter.removeAllViews()
        displayRate(items.toList())
        fullScroll(FOCUS_UP)
    }

    override fun displayRate(currencies: List<Currency>) {
        items.clear()
        items.addAll(currencies)
        val containter = findViewById<LinearLayout>(R.id.currenciesListContainer)

        if (containter.isEmpty()) {
            bindNewCurrencyList(currencies, containter)
        } else {
            updateExistingItems(containter)
        }
    }

    private fun updateExistingItems(container: LinearLayout) {
        for (index in 0 until container.childCount) {
            val view = container.getChildAt(index) as CurrenciesListItemView

            // Remove Currency if no longer available
            if (CurrenciesCache.getCache().containsKey(view.tag).not()) {
                container.removeView(view)
            } else {
                CurrenciesCache.getCache()[view.tag]?.let {
                    bindCurrencyToView(view, it)
                }
            }

            // Add new Currency values if any
            CurrenciesCache.newData().forEach {
                bindCurrencyToView(view, it)
            }
        }
    }

    private fun bindNewCurrencyList(
        currencies: List<Currency>,
        containter: LinearLayout
    ) {
        for (currency in currencies) {
            val view = CurrenciesListItemView(
                context,
                itemClicked = { setFirstResponder(currency) })
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view.layoutParams = layoutParams
            bindCurrencyToView(view, currency).let { containter.addView(it) }
            bindItemEdited(view, currency)
        }
    }

    override fun bindItemEdited(
        view: CurrenciesListItemView,
        currency: Currency
    ) {
        view.findViewById<EditText>(R.id.currencyValue).let { currentValueEditText ->
            currentValueEditText.setOnFocusChangeListener { view, hasFocus ->
                var dispatched = false
                if (hasFocus && dispatched.not()) {
                    disposable.clear()
                    disposable.add(
                        (view as EditText).textChanges()
                            .skipInitialValue()
                            .debounce(Interval.SHORT, TimeUnit.MILLISECONDS)
                            ?.map { text -> text.toString() }
                            ?.flatMap { changedValue ->
                                Observable.just(CurrencyValue(changedValue, Keyboard.isOpened))
                            }
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({ changedValue ->
                                if (changedValue.keyboardOpened) {
                                    dispatched = true
                                    updateCurrencyValues(changedValue.value, currency)
                                }
                            }, {
                                Logger.error(it)
                            })
                    )
                }
            }
        }
    }

    private fun updateCurrencyValues(changedValue: String, currentCurrency: Currency) {
        val container = this@CurrenciesListView.findViewById<LinearLayout>(R.id.currenciesListContainer)
        if (changedValue.isEmpty() || changedValue.toDouble() == ZERO) {
            setEmptyValueState(container, currentCurrency)
        } else {
            setEquivalentCurrencyValues(changedValue, currentCurrency, container)
        }
    }

    private fun setEquivalentCurrencyValues(
        changedValue: String,
        currentCurrency: Currency,
        container: LinearLayout
    ) {
        val multiplier = BigDecimal.valueOf(changedValue.toDouble()).div(BigDecimal(currentCurrency.value))
        for (index in 0 until container.childCount) {
            val map = mutableMapOf<String, Currency>()
            map.putAll(CurrenciesCache.getCache())
            val currentView = container.getChildAt(index)
            val currencyCode = currentView.findViewById<TextView>(R.id.currencyAbbreviation).text.toString()

            if (currencyCode.equals(currentCurrency.currencyCode, true).not()) {
                map[currencyCode]?.let { theCurrency ->
                    theCurrency.value = (multiplier.multiply(BigDecimal(theCurrency.value))).toDouble().toThreeDecimalPlace()
                    currentView.findViewById<EditText>(R.id.currencyValue).setText(
                        context.getString(
                            R.string.currency_value,
                            theCurrency.value.toBigDecimal().toPlainString()
                        )
                    )
                }
            }
        }

        ExchangeRateEvaluator.currencyCode = currentCurrency.currencyCode
        ExchangeRateEvaluator.value = changedValue.toDouble()
    }

    private fun setEmptyValueState(
        container: LinearLayout,
        currentCurrency: Currency
    ) {
        for (index in 0 until container.childCount) {
            val currentView = container.getChildAt(index)
            val currencyCode =
                currentView.findViewById<TextView>(R.id.currencyAbbreviation).text.toString()
            if (currencyCode.equals(currentCurrency.currencyCode, true).not()) {
                currentView.findViewById<EditText>(R.id.currencyValue).setText(EMPTY_STRING)
            }
        }
        ExchangeRateEvaluator.clear()
    }

    private fun bindCurrencyToView(
        view: CurrenciesListItemView,
        currency: Currency
    ) = view.apply {
        tag = currency.currencyCode
        currenciesListItemPresenter.attachView(this, lifeCycle)
        currenciesListItemPresenter.bindCurrencyToView(currency)
    }

    override fun showError(message: String) {
        Snackbar.make(
            this,
            context.getString(R.string.error_message, message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        flingListener.onFlingStarted()
        post(mScrollChecker)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.dispose()
    }

    private lateinit var dialog: AlertDialog
    override fun showNetworkError(errorMessage: String) {
        if (this::dialog.isInitialized) {
            if (dialog.isShowing.not()) {
                createAndShowDialog(errorMessage)
            }
        } else {
            createAndShowDialog(errorMessage)
        }
    }

    private fun createAndShowDialog(errorMessage: String) {
        dialog = initializeDialog(errorMessage)
        dialog.show()
    }

    private fun initializeDialog(errorMessage: String): AlertDialog =
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_title, ErrorMessage.dialogTitle))
            .setMessage(context.getString(R.string.dialog_message, errorMessage))
            .setPositiveButton(context.getString(R.string.ok), null)
            .create()
}

private data class CurrencyValue(val value: String, val keyboardOpened: Boolean)
