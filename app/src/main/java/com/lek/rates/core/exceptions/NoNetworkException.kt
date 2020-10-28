package com.lek.rates.core.exceptions

import com.lek.rates.globals.ErrorMessage
import java.lang.Exception

class NoNetworkException(errorMessage: String = ErrorMessage.noNetworkError) : Exception(errorMessage)
