package com.lek.rates.presentation.currencieslist.presenter

import com.lek.rates.BaseTest
import com.lek.rates.core.exceptions.NoNetworkException
import com.lek.rates.core.testutils.mockCurrencies
import com.lek.rates.globals.ErrorMessage
import com.lek.rates.presentation.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.currencieslist.interactors.GetCurrenciesInteractor
import com.lek.rates.presentation.currencieslist.stream.NetworkAvailabilityRelay
import com.lek.rates.presentation.currencieslist.stream.ShowEmptyStateRelay
import com.lek.rates.presentation.currencieslist.stream.ShowLoadingViewRelay
import com.lek.rates.presentation.currencieslist.view.ListView
import io.mockk.mockkStatic
import io.mockk.every
import io.mockk.mockk
import io.mockk.just
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.hamcrest.CoreMatchers.any
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CurrenciesCurrenciesListPresenterTest : BaseTest() {

    private val getCurrenciesInteractor: GetCurrenciesInteractor = mockk()
    private val pollingService: CurrenciesPollingService = mockk()
    private val getCanPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor = mockk()
    private val shouldShowEmptyStateRelay: ShowEmptyStateRelay = mockk()
    private val showLoadingViewRelay: ShowLoadingViewRelay = mockk()
    private val networkAvailabilityRelay: NetworkAvailabilityRelay = mockk()

    private val view: ListView = mockk()
    private val presenter = spyk(
        CurrenciesListPresenter(
            getCurrenciesInteractor,
            pollingService,
            getCanPublishLiveUpdateInteractor,
            shouldShowEmptyStateRelay,
            showLoadingViewRelay,
            networkAvailabilityRelay
        )
    )

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when CurrenciesListPresenter is invoked without error and network available - view displays currencies`(isNetworkAvailable: Boolean) {
        val currencies = mockCurrencies()
        every { view.displayRate(any()) } just runs
        every { view.showError(any()) } just runs
        every { getCanPublishLiveUpdateInteractor() } answers { Observable.just(true) }
        every { getCurrenciesInteractor() } answers { Observable.just(currencies) }
        every { pollingService() } answers { Observable.just(currencies) }
        mockStreams()
        every { networkAvailabilityRelay.get() } answers { Observable.just(isNetworkAvailable) }
        every { presenter.view() } returns view
        presenter.onStart()
        triggerActions()

        if (isNetworkAvailable) {
            verify { view.displayRate(currencies) }
            verify { showLoadingViewRelay.set(false) }
            verify { shouldShowEmptyStateRelay.set(false) }
            verify(exactly = 0) { view.showError(any()) }
        } else {
            verify(exactly = 0) { view.displayRate(currencies) }
        }
    }

    @Test
    fun `when CurrenciesListPresenter is invoked with an error - view displays error message`() {
        val errorMessage = "Error Occurred"
        every { view.displayRate(any()) } just runs
        every { view.showError(any()) } just runs
        every { getCanPublishLiveUpdateInteractor() } answers { Observable.just(true) }
        every { getCurrenciesInteractor() } answers { Observable.error(Throwable(errorMessage)) }
        every { pollingService() } answers { Observable.just(listOf()) }
        mockStreams()
        every { presenter.view() } returns view
        presenter.onStart()
        triggerActions()
        verify { view.showError(errorMessage) }
        verify(exactly = 0) { view.displayRate(any()) }
    }

    @Test
    fun `when CurrenciesListPresenter is invoked with a Network error - view displays network error message`() {
        mockkStatic(ErrorMessage::class)
        every { view.displayRate(any()) } just runs
        every { view.showError(any()) } just runs
        every { view.showNetworkError(any()) } just runs
        every { getCanPublishLiveUpdateInteractor() } answers { Observable.just(true) }
        every { getCurrenciesInteractor() } answers { Observable.error(NoNetworkException()) }
        every { pollingService() } answers { Observable.just(listOf()) }
        mockStreams()
        every { presenter.view() } returns view
        presenter.onStart()
        triggerActions()
        verify { view.showNetworkError(ErrorMessage.noNetworkError) }
        verify(exactly = 0) { view.displayRate(any()) }
    }

    private fun mockStreams() {
        every { shouldShowEmptyStateRelay.get() } answers { Observable.just(false) }
        every { showLoadingViewRelay.get() } answers { Observable.just(false) }
        every { networkAvailabilityRelay.get() } answers { Observable.just(true) }
        every { showLoadingViewRelay.set(any()) } just runs
        every { shouldShowEmptyStateRelay.set(any()) } just runs
    }
}
