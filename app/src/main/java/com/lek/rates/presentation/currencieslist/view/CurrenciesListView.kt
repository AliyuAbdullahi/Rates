package com.lek.rates.presentation.currencieslist.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import androidx.lifecycle.Lifecycle
import com.jakewharton.rxbinding4.widget.textChanges
import com.lek.rates.R
import com.lek.rates.core.data.CurrenciesCache
import com.lek.rates.core.data.CurrencyEditDispatcher
import com.lek.rates.core.models.Currency
import com.lek.rates.core.models.ExchangeRateEvaluator
import com.lek.rates.core.models.FirstResponder
import com.lek.rates.extensions.isNotSameAs
import com.lek.rates.extensions.toThreeDecimalPlace
import com.lek.rates.presentation.rateslistitem.presenter.ListItemPresenter
import com.lek.rates.presentation.currencieslist.presenter.ListPresenter
import com.lek.rates.presentation.rateslistitem.view.CurrenciesListItemView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

private const val DELAY_MILLIS = 100L

class CurrenciesListView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ScrollView(context, attributeSet, defStyle), MainView {

    private var items = LinkedList<Currency>()

    lateinit var flingListener: OnFlingListener

    private var mScrollChecker: Runnable? = null

    private var mPreviousPosition = 0

    lateinit var presenter: ListPresenter

    lateinit var listItemPresenter: ListItemPresenter

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

        CurrencyEditDispatcher.accept = {
            val current = mutableListOf<Currency>()
            current.addAll(CurrenciesCache.get())
            updateCurrencyValues(it, current)
        }
    }

    private fun updateCurrencyValues(
        currentValue: String,
        currentCurrencies: MutableList<Currency>
    ) {
        if (currentValue.isBlank()) {
            findViewById<LinearLayout>(R.id.currenciesListContainer).forEach { view ->
                val currencyCode =
                    (view as CurrenciesListItemView).findViewById<TextView>(R.id.currencyAbbreviation).text.toString()
                if (!currencyCode.equals(ExchangeRateEvaluator.currencyCode, true)) {
                    view.findViewById<TextView>(R.id.currencyValue)
                        .setText(context.getString(R.string.currency_value, ""))
                }
            }
        } else {
            currentCurrencies.forEach { currency ->
                if (currency.currencyCode isNotSameAs ExchangeRateEvaluator.currencyCode) {
                    currency.value = currency.value * ExchangeRateEvaluator.value
                }
            }
            val currencyMap = currentCurrencies.map { it.currencyCode to it }.toMap()
            findViewById<LinearLayout>(R.id.currenciesListContainer).forEach { view ->
                val currencyCode =
                    (view as CurrenciesListItemView).findViewById<TextView>(R.id.currencyAbbreviation).text.toString()
                currencyMap[currencyCode]?.let {
                    view.findViewById<EditText>(R.id.currencyValue).apply {
                        setText(
                            context.getString(
                                R.string.currency_value,
                                "${it.value.toThreeDecimalPlace()}"
                            )
                        )
                    }
                }
            }
        }
    }


    private fun setFirstResponder(currency: Currency) {
        items.remove(currency)
        items.addFirst(currency)
        FirstResponder.firstResponder = currency.currencyCode
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
        view.findViewById<EditText>(R.id.currencyValue).let {
            it.setOnFocusChangeListener { view, hasFocus ->
                var dispatched = false
                if (hasFocus && dispatched.not()) {
                    disposable.clear()
                    disposable.add(
                        (view as EditText).textChanges()
                            .skipInitialValue()
                            .debounce(300, TimeUnit.MILLISECONDS)
                            ?.map { text -> text.toString() }
                            ?.flatMap { changedValue ->
                                Observable.just(CurrencyData(changedValue, Keyboard.isOpened))
                            }
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({ changedValue ->
                                if (changedValue.keyboardOpened) {
                                    dispatched = true
                                    updateData(changedValue.value, currency)
                                }
                            }, {
                                Log.e("ERROR", "ERROR $it")
                            })
                    )
                }
            }
        }
    }

    private fun updateData(changedValue: String, currentCurrency: Currency) {
        val container = this@CurrenciesListView.findViewById<LinearLayout>(R.id.currenciesListContainer)
        if (changedValue.isEmpty() || changedValue.toDouble() == 0.0) {
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
        val multiplier = changedValue.toDouble() / currentCurrency.value
        for (index in 0 until container.childCount) {
            val map = mutableMapOf<String, Currency>()
            map.putAll(CurrenciesCache.getCache())
            val currentView = container.getChildAt(index)
            val currencyCode =
                currentView.findViewById<TextView>(R.id.currencyAbbreviation).text.toString()
            if (currencyCode.equals(currentCurrency.currencyCode, true).not()) {
                map[currencyCode]?.let { theCurrency ->
                    theCurrency.value = (theCurrency.value * multiplier).toThreeDecimalPlace()
                    currentView.findViewById<EditText>(R.id.currencyValue)
                        .setText(context.getString(R.string.currency_value, theCurrency.value))
                }
            }
        }
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
                currentView.findViewById<EditText>(R.id.currencyValue).setText("")
            }
        }
    }

    private fun bindCurrencyToView(
        view: CurrenciesListItemView,
        currency: Currency
    ) = view.apply {
        tag = currency.currencyCode
        listItemPresenter.attachView(this, lifeCycle)
        listItemPresenter.bindCurrencyToView(currency)
    }

    override fun showError(message: String) {

    }

    override fun updateCurrencies(currencies: List<Currency>) {

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
}

private data class CurrencyData(val value: String, val keyboardOpened: Boolean)
