package com.pranjaldesai.coronavirustracker.ui.shared

import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

abstract class CoreViewModel<V : ICoreView> : ViewModel() {

    lateinit var owner: LifecycleOwner
    abstract var subscribedView: V
    @CallSuper
    fun initialize() {
        addObservers(owner)
    }

    @CallSuper
    open fun release() {
        removeObservers(owner)
    }

    open fun addObservers(owner: LifecycleOwner) {}
    open fun removeObservers(owner: LifecycleOwner) {}
}

fun <V : ICoreView, VM : CoreViewModel<V>> VM.subscribe(
    view: V,
    lifecycleOwner: LifecycleOwner
): VM =
    apply {
        owner = lifecycleOwner
        subscribedView = view
        initialize()
    }
