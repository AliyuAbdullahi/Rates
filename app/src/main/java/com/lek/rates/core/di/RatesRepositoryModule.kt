package com.lek.rates.core.di

import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.data.FetchedRates
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.repository.CurrenciesRepository
import com.lek.rates.presentation.ui.currencieslist.CanPublishCurrenciesRelay
import com.lek.rates.presentation.ui.currencieslist.CurrenciesRelay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RatesRepositoryModule {

    @Provides
    fun provideRatesRepository(
        currenciesService: CurrenciesService,
        dataChangedObservable: DataChangedObservable,
        fetchedRate: FetchedRates
    ): ICurrenciesRepository = CurrenciesRepository(currenciesService, dataChangedObservable, fetchedRate)

    @Provides
    fun provideFetchedRates(): FetchedRates = FetchedRates()

    @Provides
    fun provideDataChangedObservable(): DataChangedObservable = DataChangedObservable()

    @Provides
    fun provideCurrenciesRelay(): CurrenciesRelay = CurrenciesRelay()

    @Provides
    fun provideCanPublishCurrenciesRelay(): CanPublishCurrenciesRelay = CanPublishCurrenciesRelay()
}
