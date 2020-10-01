package com.lek.rates.presentation.ui.currencieslist.interactors

import com.lek.rates.BaseTest
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.testutils.mockCurrencies
import com.lek.rates.presentation.helper.FirstResponderFactory
import com.lek.rates.presentation.ui.currencieslist.CanPublishCurrenciesRelay
import com.lek.rates.presentation.ui.currencieslist.CurrenciesRelay
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.TimeUnit

internal class CurrenciesPollingServiceTest : BaseTest() {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when CurrenciesPollingService is invoked - data is observed if CanPublishCurrencies and DataChanged`(
        enabled: Boolean
    ) {
        val currenciesRepository: ICurrenciesRepository = mockk()
        val firstResponderFactory: FirstResponderFactory = mockk()
        val canPublishCurrenciesRelay: CanPublishCurrenciesRelay = mockk()
        val currenciesRelay: CurrenciesRelay = mockk()
        val dataChangedObservable: DataChangedObservable = mockk()
        val currencies = mockCurrencies()

        mockkStatic(FirstResponderFactory::class)
        mockkStatic(TimeUnit::class)

        every { currenciesRepository.getCurrencies(any()) } answers { Observable.just(currencies) }
        every { firstResponderFactory.firstResponder } returns "EUR"
        every { currenciesRelay.get() } answers { Observable.just(currencies) }
        every { dataChangedObservable.getDataChanged() } answers { Observable.just(enabled) }
        every { canPublishCurrenciesRelay.get() } answers { Observable.just(enabled) }
        every { currenciesRelay.setCurrencies(any()) } just runs
        every { canPublishCurrenciesRelay.set(any()) } just runs

        val result = CurrenciesPollingService(
            currenciesRepository,
            firstResponderFactory,
            canPublishCurrenciesRelay,
            currenciesRelay,
            dataChangedObservable
        ).invoke().test()

        advanceScheduler(2)

        if (enabled) {
            verify { currenciesRelay.setCurrencies(currencies) }
        } else {
            verify(exactly = 0) { currenciesRelay.setCurrencies(currencies) }
        }

        result.assertNoErrors()
    }
}
