package com.lek.rates.logger

import timber.log.Timber

object Logger {
    fun warn(key: String, message: String) {
        Timber.w("$key: %s", message)
    }

    fun error(key: String, message: String) {
        Timber.e("$key: %s", message)
    }

    fun error(throwable: Throwable) {
        Timber.e(throwable)
    }

    fun debug(key: String, message: String) {
        Timber.d("$key: %s", message)
    }

    fun logTerribleFailure(error: Throwable) {
        Timber.wtf(error)
    }
}
