package com.lek.rates.globals

/**
 *  These are global Constants, available in entire application
 */
const val ZERO = 0.0
const val NEGATIVE_ONE = -1.0
const val ONE = 1
const val EMPTY_STRING = ""

object Interval {
    const val LONG = 1500L
    const val MEDIUM = 1000L
    const val DEFAULT = 500L
    const val SHORT = 300L
    const val VERY_SHORT = 100L
}

object ErrorMessage {
    const val dialogTitle = "Network Error"
    const val defaultErrorMessage = "An Error occurred while making request"
    const val noNetworkError = "No network connection found"
}
