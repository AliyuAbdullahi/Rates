package com.lek.rates.utils

import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.safeDispose() {
    if (!isDisposed) {
        dispose()
    }
}
