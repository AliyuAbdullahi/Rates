package com.lek.rates.bot

import com.lek.rates.R

class CurrenciesListBot : ScreenBot<CurrenciesListBot>() {
    fun listViewExist(): CurrenciesListBot = checkIsDisplayed(R.id.currenciesListView)
    fun clickOnCurrency(currencyCode: String): CurrenciesListBot = clickOnViewWithTag(currencyCode)
    fun checkIfCurrencyDisplayed(currencyCode: String): CurrenciesListBot =
        checkIsDisplayedWithTag(currencyCode)

    fun viewHasCurrencyAtPosition(currencyCode: String, position: Int): CurrenciesListBot =
        checkViewHasChildAtIndex(
            R.id.currencyAbbreviation,
            R.id.currenciesListContainer,
            position,
            currencyCode
        )

    fun changeCurrencyValueForCurrencyAt(position: Int, value: String): CurrenciesListBot =
        checkChildAtPositionEdited(
            R.id.currencyValue,
            R.id.currenciesListContainer,
            position,
            value
        )

    fun checkCurrencyValueAtPosition(position: Int, value: String): CurrenciesListBot = checkCildAtPostionHasValue(
        R.id.currencyValue,
        R.id.currenciesListContainer,
        position,
        value
    )
}
