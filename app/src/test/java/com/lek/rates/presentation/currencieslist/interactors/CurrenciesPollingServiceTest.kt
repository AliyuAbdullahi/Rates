package com.lek.rates.presentation.ui.currencieslist.interactors

import com.lek.rates.BaseTest
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.testutils.mockCurrencies
import com.lek.rates.core.models.FirstResponder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.TimeUnit

internal class CurrenciesPollingServiceTest : BaseTest() {

    @ParameterizedTest
    @ValueSource(longs = [0, 1])
    fun `when CurrenciesPollingService is invoked - data is observed after 1 second interval`(interval: Long) {
        val currenciesRepository: ICurrenciesRepository = mockk()
        val firstResponderFactory: FirstResponder = mockk()
        val currencies = mockCurrencies()

        mockkStatic(FirstResponder::class)
        mockkStatic(TimeUnit::class)

        every { currenciesRepository.getCurrencies(any()) } answers { Observable.just(currencies) }
        every { firstResponderFactory.firstResponder } returns "EUR"

        val result = CurrenciesPollingService(
            currenciesRepository,
            firstResponderFactory
        ).invoke().test()

        advanceScheduler(interval)

        if (interval == 0L) {
            result.assertNotComplete()
            assert(result.values().isEmpty())
        } else {
            assert(result.values()[0] == currencies)
        }
    }
}
