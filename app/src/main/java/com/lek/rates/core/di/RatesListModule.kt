package com.lek.rates.core.di

import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.presentation.helper.FirstResponderFactory
import com.lek.rates.presentation.ui.currencieslist.CanPublishCurrenciesRelay
import com.lek.rates.presentation.ui.currencieslist.CurrenciesRelay
import com.lek.rates.presentation.ui.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.ui.currencieslist.interactors.GetCurrenciesInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object RatesListModule {

    @Provides
    fun providePollingService(
        currenciesRepository: ICurrenciesRepository,
        firstResponderFactory: FirstResponderFactory,
        canPublishCurrenciesRelay: CanPublishCurrenciesRelay,
        currenciesRelay: CurrenciesRelay,
        dataChangedObservable: DataChangedObservable
    ): CurrenciesPollingService = CurrenciesPollingService(
        currenciesRepository,
        firstResponderFactory,
        canPublishCurrenciesRelay,
        currenciesRelay,
        dataChangedObservable
    )

    @Provides
    fun provideGetRatesInteractor(
        currenciesRepository: ICurrenciesRepository,
        firstResponderFactory: FirstResponderFactory
    ): GetCurrenciesInteractor = GetCurrenciesInteractor(currenciesRepository, firstResponderFactory)

    @Provides
    fun provideFirstResponderFactory(): FirstResponderFactory = FirstResponderFactory
}
