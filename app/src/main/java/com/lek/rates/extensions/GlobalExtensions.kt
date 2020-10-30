package com.lek.rates.extensions

import kotlin.math.floor
import kotlin.math.roundToLong

infix fun String.isSameAs(anotherString: String) = this.equals(anotherString, true)

infix fun String.isNotSameAs(anotherString: String) = this.isSameAs(anotherString).not()

fun Double.toThreeDecimalPlace() = floor(this * 1000) / 1000.0
