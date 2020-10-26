package com.lek.rates.presentation.rateslistitem

import com.lek.rates.BaseTest
import com.lek.rates.R
import com.lek.rates.core.models.Currency
import com.lek.rates.presentation.rateslistitem.presenter.CurrenciesListItemPresenter
import com.lek.rates.presentation.rateslistitem.view.CurrenciesListItemView
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class CurrenciesCurrenciesListItemPresenterTest : BaseTest() {

    @Test
    fun `when CurrenciesListItemPresenter-bindItem() is called - Currency items is displayed`() {
        val view: CurrenciesListItemView = mockk()

        every { view.setCurrencyValue(any()) } returns Unit
        every { view.setCurrency(any()) } returns Unit
        every { view.setFlagIcon(any()) } returns Unit
        every { view.setCurrencyCode(any()) } returns Unit

        val presenter = spyk(CurrenciesListItemPresenter())

        every { presenter.view() } returns view
        val currency = Currency("EUR", "Euro", 1.5, R.drawable.eur)
        presenter.bindCurrencyToView(currency)
        verify { view.setCurrency(currency.currencyName) }
        verify { view.setCurrencyCode(currency.currencyCode) }
        verify { view.setCurrencyValue(currency.value) }
        verify { view.setFlagIcon(currency.flag) }
    }
}
