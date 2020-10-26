package com.lek.rates.core.di

import com.lek.rates.BuildConfig
import com.lek.rates.BuildConfig.BASE_URL
import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.presentation.currencieslist.stream.KeyboardOpenedRelay
import com.lek.rates.presentation.currencieslist.stream.ViewScrollingRelay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val DEBUG = "debug"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        if (BuildConfig.BUILD_TYPE.toLowerCase(Locale.ROOT) == DEBUG) {
            getDebugHttpClient()
        } else
            getReleaseHttpClient()

    private fun getReleaseHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    private fun getDebugHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CurrenciesService =
        retrofit.create(CurrenciesService::class.java)

    @Provides
    @Singleton
    fun provideKeyboardOpenedRelay(): KeyboardOpenedRelay = KeyboardOpenedRelay()

    @Provides
    @Singleton
    fun provideViewScrollingRelay(): ViewScrollingRelay = ViewScrollingRelay()
}
