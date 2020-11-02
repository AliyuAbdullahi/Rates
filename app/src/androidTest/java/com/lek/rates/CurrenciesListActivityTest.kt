package com.lek.rates

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lek.rates.bot.CurrenciesListBot
import com.lek.rates.bot.ScreenBot.Companion.withBot
import com.lek.rates.core.api.config.ApiConfig
import com.lek.rates.helper.WaitingAction
import com.lek.rates.helper.waitForMillis
import com.lek.rates.presentation.currencieslist.view.CurrenciesListActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CurrenciesListActivityTest {

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        ApiConfig.setBaseUrl("http://127.0.0.1:8080/")
        mockWebServer.start(8080)
    }

    @Test
    fun activityLaunchSuccessfully() {
        dispatchMockServer()

        ActivityScenario.launch(CurrenciesListActivity::class.java)
    }

    @Test
    fun testListDisplayed() {
        dispatchMockServer()
        ActivityScenario.launch(CurrenciesListActivity::class.java)
        WaitingAction.wait().until {
            withBot(CurrenciesListBot::class.java)
                .listViewExist()
                .checkIsDisplayedWithTag("CHF")
        }
    }

    @Test
    fun testFirstResponderChangesOnDifferentItemSelected() {
        dispatchMockServer()
        ActivityScenario.launch(CurrenciesListActivity::class.java)
        WaitingAction.wait().until {
            withBot(CurrenciesListBot::class.java)
                .listViewExist()
                .checkIfCurrencyDisplayed("CHF")
                .clickOnCurrency("CHF")
            waitForMillis(2000)
            withBot(CurrenciesListBot::class.java).viewHasCurrencyAtPosition("CHF", 0)
        }
    }

    @Test
    fun testCurrencyEditBinding() {
        dispatchMockServer()
        ActivityScenario.launch(CurrenciesListActivity::class.java)
        WaitingAction.wait().until {
            withBot(CurrenciesListBot::class.java).changeCurrencyValueForCurrencyAt(4, "1")
                .closeKeyboard()
                .changeCurrencyValueForCurrencyAt(0, "0.666")
        }
    }

    @Test
    fun testCurrencyClearedBinding() {
        dispatchMockServer()
        ActivityScenario.launch(CurrenciesListActivity::class.java)
        WaitingAction.wait().until {
            withBot(CurrenciesListBot::class.java).changeCurrencyValueForCurrencyAt(4, "")
                .closeKeyboard()
                .changeCurrencyValueForCurrencyAt(0, "")
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private fun dispatchMockServer() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(countriesJson)
            }
        }
    }
}
