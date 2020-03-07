package com.pranjaldesai.coronavirustracker.ui.shared

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.databinding.ActivityNavigationHostBinding
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import com.pranjaldesai.coronavirustracker.ui.customView.ProgressIndicatorView

abstract class FMNavigationHostActivity :
    CoreActivity<ActivityNavigationHostBinding>(R.layout.activity_navigation_host),
    INavigationHost {

    override var subscribedFragment: IPrimaryFragment? = null

    abstract val connectivityMonitor: LiveData<Boolean>

    open val enableUpNavigation: Boolean = true

    abstract fun setupNavigation()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToConnectionMonitor()
        setupNavigation()
        setupBottomNavigationVisibility()
    }

    override fun onDestroy() {
        unsubscribeToConnectionMonitor()
        super.onDestroy()
    }

    override fun progressIndicatorView(): ProgressIndicatorView? {
        return binding.pivProgress
    }

    final override fun updateBottomNavigationSelection(@IdRes selectedOptionId: Int) {
        if (binding.bottomNavigationView.selectedItemId != selectedOptionId) {
            binding.bottomNavigationView.selectedItemId = selectedOptionId
        }
    }

    final override fun onSupportNavigateUp(): Boolean {
        return if (enableUpNavigation) {
            findNavController(R.id.nav_host_fragment).navigateUp()
        } else {
            false
        }
    }

    private fun setupBottomNavigationVisibility() {
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            if (isDestinationTopLevelFragment(destination.id)) {
                showBottomNavigation()
            } else {
                hideBottomNavigation()
            }
        }
    }

    private fun hideBottomNavigation() {
        with(binding.bottomNavigationView) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = 4
            }
        }
    }

    private fun showBottomNavigation() {
        with(binding.bottomNavigationView) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration = 4
        }
    }

    private fun subscribeToConnectionMonitor() {
        connectivityMonitor.observe(this, Observer { isConnected ->
            isConnected?.let {
                onConnectionChanged(isConnected)
            }
        })
    }

    private fun unsubscribeToConnectionMonitor() {
        connectivityMonitor.removeObservers(this)
    }

    private fun onConnectionChanged(isConnected: Boolean) {
        launchOnMain {
            if (isConnected) {
                onInternetConnectionEstablished()
            } else {
                onInternetConnectionLost()
            }
        }
    }

    @CallSuper
    open fun onInternetConnectionEstablished() {
        binding.networkLostIndicator.visibility = View.GONE
    }

    @CallSuper
    open fun onInternetConnectionLost() {
        binding.networkLostIndicator.visibility = View.VISIBLE
    }
}