package com.lek.rates.core.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 *  Android Lifecycle-ViewModel is used to provide lifecycle-aware functionality to the Presenter
 */
abstract class BasePresenter<View> : ViewModel(), LifecycleObserver {
    private var view: View? = null
    private var viewLifecycle: Lifecycle? = null

    private val disposable = CompositeDisposable()

    fun attachView(view: View, viewLifecycle: Lifecycle) {
        this.view = view
        this.viewLifecycle = viewLifecycle

        viewLifecycle.addObserver(this)
    }

    fun view(): View? {
        return view
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {}

    fun addDisposable(disposable: Disposable?) = disposable?.let { this.disposable.add(it) }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        view = null
        viewLifecycle = null
        disposable.dispose()
    }
}
