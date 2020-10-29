package com.lek.rates.helper

interface Wait {
    fun <T> until(isTrue: () -> T): T
}
