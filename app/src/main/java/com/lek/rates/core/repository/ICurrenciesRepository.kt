package com.lek.rates.core.repository

import com.lek.rates.core.models.Currency
import io.reactivex.rxjava3.core.Observable

interface IRatesRepository {
    fun getRates(baseCurrency: String): Observable<List<Currency>>
}
