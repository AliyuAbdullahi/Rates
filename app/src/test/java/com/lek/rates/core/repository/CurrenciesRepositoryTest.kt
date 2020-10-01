package com.lek.rates.core.repository

import com.lek.rates.BaseTest
import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.data.FetchedRates
import com.lek.rates.core.models.CurrenciesResponse
import com.lek.rates.core.testutils.mockCurrenciesResponse
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.api.Test

internal class CurrenciesRepositoryTest : BaseTest() {

    @Test
    fun `when CurrenciesRepository is invoked with a baseCurrency - currencies are returned`() {
        val currenciesService: CurrenciesService = mockk()
        val dataChangedObservable: DataChangedObservable = mockk()
        val fetchedRates: FetchedRates = mockk()

        val repository: ICurrenciesRepository =
            CurrenciesRepository(currenciesService, dataChangedObservable, fetchedRates)
        every { currenciesService.getLatestCurrencyRates(any()) } answers {
            Observable.just(
                mockCurrenciesResponse()
            )
        }
        every { dataChangedObservable.setDataChanged(any()) } just runs
        every { fetchedRates.set(any()) } just runs
        val rate = CurrenciesResponse()
        every { fetchedRates.get() } returns rate
        val result = repository.getCurrencies("EUR").test()
        triggerActions()
        verify { dataChangedObservable.setDataChanged(true) }
        result.assertNoErrors()
        assert(result.values().flatten().map { it.currencyName.toLowerCase() }
            .containsAll(listOf("naira", "us dollar")))
        result.assertComplete()
    }
}
