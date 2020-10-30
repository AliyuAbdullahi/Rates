package com.lek.rates.presentation.currencieslist.interactors

import com.lek.rates.BaseTest
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.testutils.mockCurrencies
import com.lek.rates.core.models.FirstResponder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.api.Test

internal class GetCurrenciesInteractorTest: BaseTest(){

    @Test
    fun `when GetRatesInteractor is invoked - available Currencies from repository can be observed`() {
        val currenciesRepository: ICurrenciesRepository = mockk()
        val firstResponderFactory: FirstResponder = mockk()
        every { firstResponderFactory.firstResponder } returns "EUR"
        val currencies = mockCurrencies()
        every { currenciesRepository.getCurrencies(any()) } answers { Observable.just(currencies) }
        mockkStatic(FirstResponder::class)
        val result = GetCurrenciesInteractor(currenciesRepository, firstResponderFactory).invoke().test()
        assert(result.values()[0] == currencies)
        result.assertNoErrors()
    }
}
