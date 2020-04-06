package com.pranjaldesai.coronavirustracker.ui.shared

import com.pranjaldesai.coronavirustracker.R

interface IPrimaryFragment {
    fun navigationHost(): INavigationHost?
    fun navigateTo(destinationFragmentResId: Int): Int {
        return when (destinationFragmentResId) {
            R.id.covidMap -> R.id.action_global_covidMap
            R.id.covidDetail -> R.id.action_global_covidDetail
            R.id.covidTip -> R.id.action_global_covidTip
            else -> R.id.covidDetail
        }
    }

    fun subscribeToNavigationHost() = navigationHost()?.subscribe(this)
    fun unsubscribeFromNavigationHost() = navigationHost()?.unsubscribe(this)
    fun updateBottomNavigationSelection(bottomNavOptionId: Int) =
        navigationHost()?.updateBottomNavigationSelection(bottomNavOptionId)
}