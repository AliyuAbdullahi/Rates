package com.lek.rates.core.di

import android.content.Context
import com.lek.rates.BuildConfig
import com.lek.rates.BuildConfig.BASE_URL
import com.lek.rates.core.api.config.ApiConfig
import com.lek.rates.core.exceptions.NoNetworkException
import com.lek.rates.core.api.services.CurrenciesService
import com.lek.rates.core.services.INetworkService
import com.lek.rates.core.services.NetworkService
import com.lek.rates.globals.Interval.VERY_SHORT
import com.lek.rates.presentation.currencieslist.stream.*
import com.lek.rates.presentation.network.NetworkStatus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val DEBUG = "debug"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(networkService: INetworkService) =
        if (BuildConfig.BUILD_TYPE.toLowerCase(Locale.ROOT) == DEBUG) {
            getDebugHttpClient(networkService)
        } else
            getReleaseHttpClient(networkService)

    @Provides
    @Singleton
    fun provideNetworkService(@ApplicationContext context: Context): INetworkService = NetworkService(context)

    private fun getReleaseHttpClient(networkService: INetworkService): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        addNetworkAvailabilityInterceptor(okHttpClientBuilder, networkService)
        return okHttpClientBuilder
            .readTimeout(VERY_SHORT, TimeUnit.SECONDS)
            .connectTimeout(VERY_SHORT, TimeUnit.SECONDS)
            .build()
    }

    private fun getDebugHttpClient(networkService: INetworkService): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClientBuilder = OkHttpClient.Builder()
        addNetworkAvailabilityInterceptor(okHttpClientBuilder, networkService)
        return okHttpClientBuilder
            .addInterceptor(logger)
            .readTimeout(VERY_SHORT, TimeUnit.SECONDS)
            .connectTimeout(VERY_SHORT, TimeUnit.SECONDS)
            .build()
    }

    private fun addNetworkAvailabilityInterceptor(
        okHttpClientBuilder: OkHttpClient.Builder,
        networkService: INetworkService
    ) {
        okHttpClientBuilder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            if (networkService.isConnected()) {
                chain.proceed(chain.request())
            } else {
                throw NoNetworkException()
            }
        })
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
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

    @Provides
    @Singleton
    fun provideShowEmptyStateRelay(): ShowEmptyStateRelay = ShowEmptyStateRelay()

    @Provides
    @Singleton
    fun provideShowLoadingViewRelay(): ShowLoadingViewRelay = ShowLoadingViewRelay()

    @Provides
    @Singleton
    fun provideNetworkStatus(): NetworkStatus = NetworkStatus()

    @Provides
    @Singleton
    fun provideNetworkAvailabilityRelay(): NetworkAvailabilityRelay = NetworkAvailabilityRelay()
}
