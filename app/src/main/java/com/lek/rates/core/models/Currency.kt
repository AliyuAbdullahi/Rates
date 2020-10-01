package com.lek.rates.core.models

import androidx.annotation.DrawableRes

data class Rate(
    val currencyCode: String,
    val currencyName: String,
    val value: Double,
    @DrawableRes val flag: Int
)
