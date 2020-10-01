package com.lek.rates.presentation.ui.rateslistitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.lek.rates.R
import com.lek.rates.core.models.Currency

class RatesListAdapter(
    private val presenter: RatesListItemPresenter,
    private val lifecycle: Lifecycle,
    private var currencies: List<Currency>
) : RecyclerView.Adapter<RatesListItemViewHolder>() {

    fun updateCurrencies(currencies: List<Currency>) {
        this.currencies = currencies
        notifyItemRangeChanged(0, currencies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesListItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_list_item, parent, false)
        return RatesListItemViewHolder(view)
    }

    override fun getItemCount(): Int = currencies.count()

    override fun onBindViewHolder(holder: RatesListItemViewHolder, position: Int) {
        presenter.attachView(holder, lifecycle)
        presenter.bindItem(currencies[position])
    }
}
