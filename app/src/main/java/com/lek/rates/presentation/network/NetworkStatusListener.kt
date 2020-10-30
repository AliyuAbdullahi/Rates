package com.lek.rates.presentation.network

interface NetworkStatusListener {
    fun networkAvailable()
    fun networkLost()
}
