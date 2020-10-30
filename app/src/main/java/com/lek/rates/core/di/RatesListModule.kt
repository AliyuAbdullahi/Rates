package com.lek.rates.core.di

import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.core.models.FirstResponder
import com.lek.rates.core.repository.ICurrenciesRepository
import com.lek.rates.presentation.currencieslist.interactors.CurrenciesPollingService
import com.lek.rates.presentation.currencieslist.interactors.GetCanPublishLiveUpdateInteractor
import com.lek.rates.presentation.currencieslist.interactors.GetCurrenciesInteractor
import com.lek.rates.presentation.currencieslist.stream.KeyboardOpenedRelay
import com.lek.rates.presentation.currencieslist.stream.ViewScrollingRelay
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
        firstResponderFactory: FirstResponder
    ): CurrenciesPollingService = CurrenciesPollingService(
        currenciesRepository,
        firstResponderFactory
    )

    @Provides
    fun provideGetRatesInteractor(
        currenciesRepository: ICurrenciesRepository,
        firstResponderFactory: FirstResponder
    ): GetCurrenciesInteractor =
        GetCurrenciesInteractor(currenciesRepository, firstResponderFactory)

    @Provides
    fun provideGetCanPublishLiveUpdateInteractor(
        keyboardOpenedRelay: KeyboardOpenedRelay,
        viewScrollingRelay: ViewScrollingRelay,
        dataChangedObservable: DataChangedObservable
    ): GetCanPublishLiveUpdateInteractor =
        GetCanPublishLiveUpdateInteractor(keyboardOpenedRelay, viewScrollingRelay, dataChangedObservable)

    @Provides
    fun provideFirstResponderFactory(): FirstResponder = FirstResponder
}
