package com.lek.rates.helper

import android.os.SystemClock
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val DEFAULT_WAIT_IN_MILLIS: Long = 500L
const val DEFAULT_TIMEOUT: Long = 10L
const val TIMEOUT_MESSAGE: String = "Expected condition failed, tried for %d seconds."

/**
 * [GentleWait] makes provision for the OS to wait for a [timeoutInSeconds] period
 * while an action is performed, if this time ellapse and the action is still null, then an exception is thrown
 */
class GentleWait(
    private val timeoutInSeconds: Long = DEFAULT_TIMEOUT,
    private val intervalInMillis: Long = DEFAULT_WAIT_IN_MILLIS,
    private val timeUnit: TimeUnit = TimeUnit.MILLISECONDS
) : Wait {

    override fun <T> until(isTrue: () -> T): T {
        var lastException: Throwable?
        val end = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutInSeconds)

        while (true) {
            lastException = try {
                val value: T = isTrue()

                if (value != null) {
                    return value
                }
                null
            } catch (e: Throwable) {
                e
            }

            val currentTime = System.currentTimeMillis()
            if (currentTime > end) {
                val timeoutMessage: String = String.format(TIMEOUT_MESSAGE, timeoutInSeconds)
                val timeoutException: Exception = TimeoutException(timeoutMessage)

                lastException?.let { timeoutException.initCause(lastException) }

                throw timeoutException
            }

            waitForMillis(timeUnit.toMillis(intervalInMillis))
        }
    }
}

fun waitForMillis(timeInMillis: Long) {
    SystemClock.sleep(timeInMillis)
}

object WaitingAction {
    fun wait(): Wait = GentleWait()
}
