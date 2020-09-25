package com.lek.rates.core.services

import com.lek.rates.core.models.RatesResponse
import com.lek.rates.core.services.Routes.LATEST
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApiService {

    @GET(LATEST)
    fun getLatestCurrencyRates(@Query("base") baseCurrency: String): Observable<RatesResponse>
}
