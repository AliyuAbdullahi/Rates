package com.lek.rates.presentation.currencieslist.presenter

import com.lek.rates.BaseTest
import com.lek.rates.core.testutils.mockCurrencies
import com.lek.rates.presentation.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.currencieslist.interactors.GetCurrenciesInteractor
import com.lek.rates.presentation.currencieslist.view.ListView
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.api.Test

internal class CurrenciesCurrenciesListPresenterTest : BaseTest() {

    private val getCurrenciesInteractor: GetCurrenciesInteractor = mockk()
    private val pollingService: CurrenciesPollingService = mockk()
    private val getCanPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor = mockk()

    private val view: ListView = mockk()
    private val presenter = spyk(CurrenciesListPresenter(getCurrenciesInteractor, pollingService, getCanPublishLiveUpdateInteractor))

    @Test
    fun `when CurrenciesListPresenter is invoked without error - view displays currencies`() {
        val currencies = mockCurrencies()
        every { view.displayRate(any()) } just runs
        every { view.showError(any()) } just runs
        every { getCanPublishLiveUpdateInteractor() } answers { Observable.just(true) }
        every { getCurrenciesInteractor() } answers { Observable.just(currencies) }
        every { pollingService() } answers { Observable.just(currencies) }
        every { presenter.view() } returns view
        presenter.onCreate()
        presenter.onStart()
        triggerActions()
        verify { view.displayRate(currencies) }
        verify(exactly = 0) { view.showError(any()) }
    }

    @Test
    fun `when CurrenciesListPresenter is invoked with an error - view displays error message`() {
        val errorMessage = "Error Occurred"
        every { view.displayRate(any()) } just runs
        every { view.showError(any()) } just runs
        every { getCanPublishLiveUpdateInteractor() } answers { Observable.just(true) }
        every { getCurrenciesInteractor() } answers { Observable.error(Throwable(errorMessage)) }
        every { pollingService() } answers { Observable.just(listOf()) }
        every { presenter.view() } returns view
        presenter.onCreate()
        triggerActions()
        verify { view.showError(errorMessage) }
        verify(exactly = 0) { view.displayRate(any()) }
    }
}
