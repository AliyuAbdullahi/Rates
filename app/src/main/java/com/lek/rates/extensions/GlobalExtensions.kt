package com.lek.rates.extensions

import kotlin.math.floor

infix fun String.isSameAs(anotherString: String) = this.equals(anotherString, true)

infix fun String.isNotSameAs(anotherString: String) = !this.isSameAs(anotherString)

fun Double.toThreeDecimalPlace() = floor(this * 1000) / 1000.0
