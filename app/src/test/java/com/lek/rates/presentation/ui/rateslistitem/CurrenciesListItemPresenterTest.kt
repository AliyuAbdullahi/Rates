package com.lek.rates.presentation.ui.rateslistitem

import com.lek.rates.BaseTest
import com.lek.rates.R
import com.lek.rates.core.models.Currency
import io.mockk.*
import org.junit.jupiter.api.Test

internal class CurrenciesListItemPresenterTest: BaseTest() {

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
        presenter.bindItem(currency)
        verify { view.setCurrency(currency.currencyName) }
        verify { view.setCurrencyCode(currency.currencyCode) }
        verify { view.setCurrencyValue(currency.value) }
        verify { view.setFlagIcon(currency.flag) }
    }
}