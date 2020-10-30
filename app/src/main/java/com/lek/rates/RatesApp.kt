package com.lek.rates

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class RatesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }

    companion object{
        lateinit var context: Context
    }
}
