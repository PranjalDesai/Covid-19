package com.pranjaldesai.coronavirustracker.ui.shared

import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

abstract class CoreViewModel<V : ICoreView> : ViewModel() {

    lateinit var owner: LifecycleOwner
    abstract var subscribedView: V

    // private set
    // TODO: Reintroduce private setter after this bug is resolved:
    // See:
    //      https://discuss.kotlinlang.org/t/kotlin-creates-a-synthetic-method-that-exposes-a-private-setter-from-superclass/6327
    //      https://youtrack.jetbrains.com/issue/KT-22465
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