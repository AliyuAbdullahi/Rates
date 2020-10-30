package com.lek.rates.presentation.currencieslistitem.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jakewharton.rxbinding4.InitialValueObservable
import com.jakewharton.rxbinding4.widget.textChanges
import com.lek.rates.R
import com.lek.rates.globals.EMPTY_STRING
import com.lek.rates.globals.ZERO
import io.reactivex.rxjava3.core.Observable

class CurrenciesListItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = ZERO.toInt(),
    val itemClicked: () -> Unit = {}
) : ConstraintLayout(context, attributeSet, defStyle), ListItemView,
    View.OnClickListener {
    
    init {
        inflate(context, R.layout.view_list_item, this)
        findViewById<View>(R.id.itemSelector).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        itemClicked()
    }

    override fun setFlagIcon(flagIcon: Int) {
        findViewById<ImageView>(R.id.flag).apply { setImageResource(flagIcon) }
    }

    override fun setCurrency(currency: String) {
        findViewById<TextView>(R.id.currencyName).apply { setText(currency) }
    }

    override fun setCurrencyCode(currencyCode: String) {
        findViewById<TextView>(R.id.currencyAbbreviation).apply { setText(currencyCode) }
    }

    override fun setCurrencyValue(currencyValue: Double) {
        findViewById<EditText>(R.id.currencyValue).apply {
            if (currencyValue <= ZERO.toInt()) {
                setText(context.getString(R.string.currency_value, EMPTY_STRING))
            } else {
                setText(context.getString(R.string.currency_value, currencyValue.toBigDecimal().toPlainString()))
            }
        }
    }

    override fun textChanged(): InitialValueObservable<CharSequence> =
        findViewById<EditText>(R.id.currencyValue).textChanges()

    override fun isInEditingMode(): Observable<Boolean> =
        Observable.just(findViewById<EditText>(R.id.currencyValue).hasFocus())
}
