package com.lek.rates.presentation.ui.rateslistitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.lek.rates.R
import com.lek.rates.core.models.Currency
import com.lek.rates.utils.safeDispose
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CurrenciesListAdapter(
    private val presenter: CurrenciesListItemPresenter,
    private val lifecycle: Lifecycle,
    private var currencies: List<Currency>
) : RecyclerView.Adapter<CurrenciesListItemViewHolder>() {

    fun updateCurrencies(currencies: List<Currency>) {
        this.currencies = currencies
        notifyItemRangeChanged(0, currencies.size)
    }

    override fun getItemId(position: Int): Long {
        return currencies[position].id ?: super.getItemId(position)
    }

    override fun onViewDetachedFromWindow(holder: CurrenciesListItemViewHolder) {
        holder.safeDispose()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrenciesListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_list_item, parent, false)
        return CurrenciesListItemViewHolder(view)
    }

    override fun getItemCount(): Int = currencies.count()

    override fun onBindViewHolder(holder: CurrenciesListItemViewHolder, position: Int) {
        presenter.attachView(holder, lifecycle)
        presenter.bindItem(currencies[position])
    }
}
