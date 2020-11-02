package com.lek.rates.logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        error?.let { thrownError -> FirebaseCrashlytics.getInstance().recordException(thrownError) }
        FirebaseCrashlytics.getInstance().log(message)
    }
}
