package com.lek.rates.core.di

import android.content.Context
import com.lek.rates.core.repository.IRatesRepository
import com.lek.rates.core.repository.RatesRepository
import com.lek.rates.presentation.rateslist.interactors.GetRatesInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object RatesListModule {

    @Provides
    fun provideGetRatesInteractor() = GetRatesInteractor()

    @Provides
    fun provideRatesRepository(@ApplicationContext context: Context): IRatesRepository =
        RatesRepository()
}
