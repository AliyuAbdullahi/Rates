package com.lek.rates.core.models

import android.content.Context
import com.lek.rates.RatesApp

private const val prefName = "com.lek.FirstResponder"
private const val KEY_FIRST_RESPONDER = "FIRST_RESPONDER"

private const val DEFAULT_RESPONDER = "EUR"
object FirstResponder {
    var firstResponder: String
        get() = getSavedFirstResponder()
        set(value) {
            setTheFirstResponder(value)
        }

    var value = 1.0

    private val pref = RatesApp.context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    private fun getSavedFirstResponder(): String {
        return pref.getString(KEY_FIRST_RESPONDER, DEFAULT_RESPONDER) ?: DEFAULT_RESPONDER
    }

    private fun setTheFirstResponder(firstResponder: String) {
        pref.edit().putString(KEY_FIRST_RESPONDER, firstResponder).apply()
    }
}
