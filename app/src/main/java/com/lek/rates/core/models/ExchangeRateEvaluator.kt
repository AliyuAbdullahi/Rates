package com.lek.rates.core.models

private const val NO_MODIFIER = NEGATIVE_ONE
const val EMPTY_STATE = ZERO

object ExchangeRateEvaluator {
    var currencyCode: String = ""
    var value: Double = NO_MODIFIER

    var isSetEmpty: Boolean = false

    fun hasNoValue() = value == NO_MODIFIER

    fun isBlank() = value == EMPTY_STATE

    fun clear() {
        currencyCode = ""
        value = NO_MODIFIER
    }
}
