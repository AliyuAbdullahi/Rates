package com.lek.rates.presentation.ui.rateslistitem

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lek.rates.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class RatesListItemViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view), RatesListItemView, Disposable {

    private val compositeDisposable = CompositeDisposable()

    override fun setFlagIcon(flagIcon: Int) {
        view.findViewById<ImageView>(R.id.flag).apply { setImageResource(flagIcon) }
    }

    override fun setCurrency(currency: String) {
        view.findViewById<TextView>(R.id.currencyName).apply { text = currency }
    }

    override fun setCurrencyCode(currencyCode: String) {
        view.findViewById<TextView>(R.id.currencyAbbreviation).apply { text = currencyCode }
    }

    override fun isDisposed(): Boolean {
        return false
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }
}
