package com.pranjaldesai.coronavirustracker.ui.shared

import com.pranjaldesai.coronavirustracker.R

interface IPrimaryFragment {
    fun navigationHost(): INavigationHost?
    fun navigateTo(destinationFragmentResId: Int): Int {
        return when (destinationFragmentResId) {
            R.id.fragmentOne -> R.id.action_global_fragmentOne
            R.id.fragmentTwo -> R.id.action_global_fragmentTwo
            else -> R.id.fragmentTwo
        }
    }

    fun subscribeToNavigationHost() = navigationHost()?.subscribe(this)
    fun unsubscribeFromNavigationHost() = navigationHost()?.unsubscribe(this)
    fun updateBottomNavigationSelection(bottomNavOptionId: Int) =
        navigationHost()?.updateBottomNavigationSelection(bottomNavOptionId)
}