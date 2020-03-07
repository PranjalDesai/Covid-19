package com.pranjaldesai.coronavirustracker.ui.shared

import androidx.annotation.IdRes

interface INavigationHost {

    var subscribedFragment: IPrimaryFragment?

    val primaryFragmentResIdList: List<Int>

    fun subscribe(fragment: IPrimaryFragment) {
        if (subscribedFragment != fragment) {
            subscribedFragment = fragment
        }
    }

    fun unsubscribe(fragment: IPrimaryFragment) {
        if (subscribedFragment == fragment) {
            subscribedFragment = null
        }
    }

    fun isDestinationTopLevelFragment(destination: Int): Boolean =
        destination in primaryFragmentResIdList

    fun updateBottomNavigationSelection(@IdRes selectedOptionId: Int)
}