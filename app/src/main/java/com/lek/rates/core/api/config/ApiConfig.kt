package com.lek.rates.core.api.config

import com.lek.rates.BuildConfig

object ApiConfig {
    var BASE_URL = BuildConfig.BASE_URL

    fun setBaseUrl(url: String) {
        BASE_URL = url
    }
}
