package com.lek.rates.core.models

private const val NO_MODIFIER = -1.0
private const val FLAGGED_EMPTY = -2.0

object CurrencyModifier {
    var currencyCode: String = ""
    var value: Double = NO_MODIFIER

    fun exist() = currencyCode.isNotEmpty()

    fun isZero() = value == 0.0

    fun isFlaggedEmpty() = value == FLAGGED_EMPTY

    fun clear() {
        currencyCode = ""
        value = NO_MODIFIER
    }
}
