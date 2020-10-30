package com.lek.rates.core.di

import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.data.FetchedRates
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.core.repository.CurrenciesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RatesRepositoryModule {

    @Provides
    @Singleton
    fun provideRatesRepository(
        currenciesService: CurrenciesService,
        dataChangedObservable: DataChangedObservable,
        fetchedRate: FetchedRates
    ): ICurrenciesRepository = CurrenciesRepository(currenciesService, dataChangedObservable, fetchedRate)

    @Provides
    @Singleton
    fun provideFetchedRates(): FetchedRates = FetchedRates()

    @Provides
    @Singleton
    fun provideDataChangedObservable(): DataChangedObservable = DataChangedObservable()
}
