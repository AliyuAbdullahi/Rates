package com.lek.rates

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class RatesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        contextRef = WeakReference(this)
    }

    companion object{
        var contextRef: WeakReference<Context>? = null
    }
}
