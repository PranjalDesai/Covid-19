package com.pranjaldesai.coronavirustracker.lifecycle


import com.pranjaldesai.coronavirustracker.extension.logTag
import com.pranjaldesai.coronavirustracker.ui.shared.DataboundWindow
import timber.log.Timber

fun DataboundWindow<*>.created() {
    updateLifecycleState(LifecycleState.CREATED)
}

fun DataboundWindow<*>.started() {
    updateLifecycleState(LifecycleState.STARTED)
}

fun DataboundWindow<*>.stopped() {
    updateLifecycleState(LifecycleState.STOPPED)
}

fun DataboundWindow<*>.paused() {
    updateLifecycleState(LifecycleState.PAUSED)
}

fun DataboundWindow<*>.resumed() {
    updateLifecycleState(LifecycleState.RESUMED)
}

fun DataboundWindow<*>.viewDestroyed() {
    updateLifecycleState(LifecycleState.VIEW_DESTROYED)
}

fun DataboundWindow<*>.viewCreated() {
    updateLifecycleState(LifecycleState.VIEW_CREATED)
}

fun DataboundWindow<*>.finished() {
    updateLifecycleState(LifecycleState.FINISHED)
}

fun DataboundWindow<*>.destroyed() {
    updateLifecycleState(LifecycleState.DESTROYED)
}

fun DataboundWindow<*>.logLifecycleEvent(state: LifecycleState) {
    if (logLifecycleEvents) {
        Timber
            .tag(logTag())
            .d(state.toString())
    }
}