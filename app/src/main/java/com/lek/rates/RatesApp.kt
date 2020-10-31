package com.lek.rates

import android.app.Application
import android.content.Context
import com.lek.rates.logger.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.lang.ref.WeakReference


@HiltAndroidApp
class RatesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        contextRef = WeakReference(this)
        plantLoggingTree()
    }

    private fun plantLoggingTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    companion object{
        var contextRef: WeakReference<Context>? = null
    }
}
