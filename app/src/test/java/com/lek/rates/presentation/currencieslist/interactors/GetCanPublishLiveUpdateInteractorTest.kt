package com.lek.rates.presentation.currencieslist.interactors

import com.lek.rates.BaseTest
import com.lek.rates.core.data.DataChangedObservable
import com.lek.rates.presentation.currencieslist.stream.KeyboardOpenedRelay
import com.lek.rates.presentation.currencieslist.stream.ViewScrollingRelay
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GetCanPublishLiveUpdateInteractorTest : BaseTest() {

    private val keyboardOpenedRelay: KeyboardOpenedRelay = mockk()
    private val viewScrollingRelay: ViewScrollingRelay = mockk()
    private val dataChangedObservable: DataChangedObservable = mockk()

    private val getCanPublishLiveUpdateInteractor: GetCanPublishLiveUpdateInteractor =
        spyk(
            GetCanPublishLiveUpdateInteractor(
                keyboardOpenedRelay,
                viewScrollingRelay,
                dataChangedObservable
            )
        )

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when keyboard is not opened - GetCanPublishLiveUpdateInteractor returns data`(
        keyboardOpened: Boolean
    ) {
        every { keyboardOpenedRelay.get() } answers { Observable.just(keyboardOpened) }
        every { viewScrollingRelay.get() } answers { Observable.just(false) }
        every { dataChangedObservable.getDataChanged() } answers { Observable.just(true) }

        val test = getCanPublishLiveUpdateInteractor().test()
        triggerActions()

        if (keyboardOpened) {
            assert(test.values()[0] == false)
        } else {
            assert(test.values()[0] == true)
        }
    }


    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when view not scrolling - GetCanPublishLiveUpdateInteractor returns data`(viewScrolling: Boolean) {
        every { keyboardOpenedRelay.get() } answers { Observable.just(false) }
        every { viewScrollingRelay.get() } answers { Observable.just(viewScrolling) }
        every { dataChangedObservable.getDataChanged() } answers { Observable.just(true) }

        val test = getCanPublishLiveUpdateInteractor().test()
        triggerActions()
        if (viewScrolling) {
            assert(test.values()[0] == false)
        } else {
            assert(test.values()[0] == true)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when there is data changed - GetCanPublishLiveUpdateInteractor returns data`(dataChanged: Boolean) {
        every { keyboardOpenedRelay.get() } answers { Observable.just(false) }
        every { viewScrollingRelay.get() } answers { Observable.just(false) }
        every { dataChangedObservable.getDataChanged() } answers { Observable.just(dataChanged) }

        val test = getCanPublishLiveUpdateInteractor().test()
        triggerActions()
        if (dataChanged) {
            assert(test.values()[0] == true)
        } else {
            assert(test.values()[0] == false)
        }
    }
}
