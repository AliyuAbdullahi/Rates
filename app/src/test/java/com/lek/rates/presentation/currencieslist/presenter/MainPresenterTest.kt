package com.lek.rates.presentation.currencieslist.presenter

import com.lek.rates.BaseTest
import com.lek.rates.presentation.currencieslist.stream.ShowEmptyStateRelay
import com.lek.rates.presentation.currencieslist.stream.ShowLoadingViewRelay
import com.lek.rates.presentation.currencieslist.view.MainView
import io.mockk.*
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MainPresenterTest : BaseTest() {
    private val shouldShowLoadingViewRelay: ShowLoadingViewRelay = mockk()
    private val shouldShowEmptyStateRelay: ShowEmptyStateRelay = mockk()
    private val mainPresenter: MainPresenter = spyk(MainPresenter(shouldShowLoadingViewRelay, shouldShowEmptyStateRelay))
    private val mainView: MainView = mockk()

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when MainPresenter is invoked - view shows correct loading state`(shouldShowLoading: Boolean) {
        every { shouldShowLoadingViewRelay.get() } answers { Observable.just(shouldShowLoading) }
        every { shouldShowEmptyStateRelay.get() } answers { Observable.just(false) }
        every { mainPresenter.view() } returns mainView
        mainPresenter.onStart()
        triggerActions()
        verify { mainView.showLoading(shouldShowLoading) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when MainPresenter is invoked - view shows correct empty state status`(shouldShowEmptyState: Boolean) {
        every { shouldShowLoadingViewRelay.get() } answers { Observable.just(false) }
        every { shouldShowEmptyStateRelay.get() } answers { Observable.just(shouldShowEmptyState) }
        every { mainView.showLoading(any()) } just runs
        every { mainPresenter.view() } returns mainView
        mainPresenter.onStart()
        triggerActions()
        verify { mainView.showEmptyState(shouldShowEmptyState) }
    }
}
