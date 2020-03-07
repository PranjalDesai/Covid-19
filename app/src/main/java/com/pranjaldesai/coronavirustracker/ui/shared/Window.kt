package com.pranjaldesai.coronavirustracker.ui.shared

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import com.pranjaldesai.coronavirustracker.lifecycle.LifecycleState
import com.pranjaldesai.coronavirustracker.ui.customView.ProgressIndicatorView

private const val UNKNOWN_RESOURCE_VALUE = 0

interface Window : CustomView {
    val windowContext: Context?

    val menuResourceId: Int?

    val logLifecycleEvents: Boolean

    fun resolveThemeProperty(@AttrRes id: Int): Int {
        val theme = windowContext?.theme
        return theme?.let {
            val typedValue = TypedValue()
            it.resolveAttribute(id, typedValue, false)
            typedValue.data
        } ?: UNKNOWN_RESOURCE_VALUE
    }


    fun <V : View> findViewById(id: Int): V

    fun runOnUiThread(task: Runnable)

    fun runOnUiThread(task: Runnable, delayInMillis: Long)

    fun showProgressIndicator() {}

    fun hideProgressIndicator() {}

    fun progressIndicatorView(): ProgressIndicatorView?

    fun updateLifecycleState(state: LifecycleState)

    fun onLifecycleStateChanged(state: LifecycleState)

}