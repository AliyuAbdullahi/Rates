package com.lek.rates.core.models

import androidx.annotation.DrawableRes

data class Currency(
    val currencyCode: String,
    val currencyName: String,
    val value: Double,
    @DrawableRes val flag: Int
) {
    var id: Long? = null
}
