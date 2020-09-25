package com.lek.rates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lek.rates.core.services.RatesApiService
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var service: RatesApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        service.getLatestCurrencyRates("EUR")
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({})
    }
}
