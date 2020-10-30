package com.lek.rates

import android.util.Log
import com.lek.rates.TestSchedulerFactory.testScheduler
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.util.concurrent.TimeUnit

abstract class BaseTest {

    fun advanceScheduler(seconds: Long = 1) {
        testScheduler.advanceTimeBy(seconds, TimeUnit.SECONDS)
    }

    fun advanceTimeBy(delay: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        testScheduler.advanceTimeBy(delay, unit)
    }

    fun triggerActions() {
        testScheduler.triggerActions()
    }

    fun advanceSchedulersAndTriggerActions() {
        advanceScheduler()
        triggerActions()
    }

    companion object {
        @BeforeAll
        @BeforeClass
        @JvmStatic
        fun setupTestScheduler() {
            MockKAnnotations.init(this)
            mockkStatic(Log::class)
            every { Log.e(any(), any()) } returns 0
            every { Log.d(any(), any()) } returns 0
            RxJavaPlugins.setInitIoSchedulerHandler { testScheduler }
            RxJavaPlugins.setInitComputationSchedulerHandler { testScheduler }
            RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        }

        @AfterAll
        @AfterClass
        @JvmStatic
        fun resetRxPlugins() {
            RxJavaPlugins.reset()
            RxAndroidPlugins.reset()
        }
    }
}
