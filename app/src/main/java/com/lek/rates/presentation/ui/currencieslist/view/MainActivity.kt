package com.lek.rates.presentation.ui.currencieslist.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lek.rates.R
import com.lek.rates.core.models.Currency
import com.lek.rates.presentation.ui.currencieslist.presenter.CurrenciesListPresenter
import com.lek.rates.presentation.ui.rateslistitem.CurrenciesListAdapter
import com.lek.rates.presentation.ui.rateslistitem.CurrenciesListItemPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RatesListView {

    private val presenter: CurrenciesListPresenter by viewModels()
    private val currenciesListItemPresenter: CurrenciesListItemPresenter by viewModels()

    private lateinit var currenciesListAdapter: CurrenciesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
    }

    private fun setUpView() {
        presenter.attachView(this, lifecycle)
        currenciesListAdapter = CurrenciesListAdapter(currenciesListItemPresenter, lifecycle, listOf())
        currenciesList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = currenciesListAdapter
        }
    }

    override fun displayRate(currencies: List<Currency>) {
        currenciesListAdapter.updateCurrencies(currencies)
    }

    override fun showError(message: String) {
        Toast.makeText(this, "Rates are $message", Toast.LENGTH_SHORT).show()
    }
}
