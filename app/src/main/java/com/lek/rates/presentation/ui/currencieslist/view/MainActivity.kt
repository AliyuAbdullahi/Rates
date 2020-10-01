package com.lek.rates.presentation.ui.rateslist.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lek.rates.R
import com.lek.rates.core.models.Currency
import com.lek.rates.presentation.ui.rateslist.presenter.RatesListPresenter
import com.lek.rates.presentation.ui.rateslistitem.CurrenciesListAdapter
import com.lek.rates.presentation.ui.rateslistitem.CurrenciesListItemPresenter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    RatesListView {

    val presenter: RatesListPresenter by viewModels()
    val currenciesListItemPresenter: CurrenciesListItemPresenter by viewModels()

    private lateinit var adapter: CurrenciesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this, lifecycle)
        adapter = CurrenciesListAdapter(currenciesListItemPresenter, lifecycle, listOf())
    }

    override fun displayRate(currencies: List<Currency>) {
        adapter.updateCurrencies(currencies)
    }

    override fun showError(message: String) {
        Toast.makeText(this, "Rates are $message", Toast.LENGTH_SHORT).show()
    }
}
