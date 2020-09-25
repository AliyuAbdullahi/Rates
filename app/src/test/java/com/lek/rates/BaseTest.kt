package com.lek.rates

import com.lek.rates.TestSchedulerFactory.testScheduler
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
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
