package com.lek.rates.core.models

import androidx.annotation.DrawableRes

data class Currency(
    val currencyCode: String,
    val currencyName: String,
    var value: Double,
    @DrawableRes val flag: Int
) {
    var id: Long? = null
}
