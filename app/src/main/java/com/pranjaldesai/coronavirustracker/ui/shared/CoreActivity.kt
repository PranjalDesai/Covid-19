package com.pranjaldesai.coronavirustracker.ui.shared

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.pranjaldesai.coronavirustracker.lifecycle.LifecycleState
import com.pranjaldesai.coronavirustracker.ui.customView.ProgressIndicatorView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class CoreActivity<ViewDataBindingT : ViewDataBinding>(
    override val layoutResourceId: Int,
    override val menuResourceId: Int? = null
) : AppCompatActivity(),
    DataboundWindow<ViewDataBindingT>, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override val windowContext: Context? by lazy { this }
    override val binding: ViewDataBindingT by lazy { attachBindingLayout() }
    override val logLifecycleEvents: Boolean = true

    private lateinit var rootView: View

    private var currentState: LifecycleState = LifecycleState.UNKNOWN


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::rootView.isInitialized) {
            rootView = binding.root
            bindData()
        }
    }

    final override fun attachLayout() {}

    final override fun attachBindingLayout(): ViewDataBindingT =
        DataBindingUtil.setContentView(this, layoutResourceId)

    @CallSuper
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return menuResourceId?.let { menuResId ->
            menu?.let { menu ->
                menuInflater.inflate(menuResId, menu)
                true
            }
        } ?: super.onCreateOptionsMenu(menu)
    }

    @CallSuper
    override fun onLifecycleStateChanged(state: LifecycleState) {
    }

    override fun bindData() {
        /* Stub */
    }

    override fun progressIndicatorView(): ProgressIndicatorView? {
        return null
    }

    final override fun updateLifecycleState(state: LifecycleState) {
        currentState = state
    }

    final override fun gatherControls() {
        /* We don't need to gather controls in data bound windows or views, since the binding object handles that */
    }

    final override fun runOnUiThread(task: Runnable, delayInMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed(task, delayInMillis)
    }
}