package com.lek.rates.core.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.ref.WeakReference

/**
 *  Android Lifecycle-ViewModel is used to provide lifecycle-aware functionality to the Presenter
 */
abstract class BasePresenter<View> : ViewModel(), LifecycleObserver {
    private var view: WeakReference<View>? = null
    private var viewLifecycle: WeakReference<Lifecycle>? = null

    private val compositeDisposable = CompositeDisposable()

    fun attachView(view: View, lifecycle: Lifecycle) {
        this.view = WeakReference(view)
        this.viewLifecycle = WeakReference(lifecycle)

        viewLifecycle?.get()?.addObserver(this)
    }

    fun view(): View? {
        return view?.get()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {}

    fun addDisposable(disposable: Disposable?) = disposable?.let { compositeDisposable.add(it) }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onViewDestroyed() {
        view?.clear()
        viewLifecycle?.clear()
        compositeDisposable.dispose()
    }
}
