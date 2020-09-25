package com.lek.rates.core.api.services

import com.lek.rates.core.models.RatesResponse
import com.lek.rates.core.api.query.QueryParams
import com.lek.rates.core.api.routes.Routes.LATEST
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApiService {

    @GET(LATEST)
    fun getLatestCurrencyRates(@Query(QueryParams.BASE) baseCurrency: String): Observable<RatesResponse>
}
