package com.pranjaldesai.coronavirustracker.ui

import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.databinding.FragmentTwoBinding
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment

class FragmentTwo : CoreFragment<FragmentTwoBinding>(), IPrimaryFragment {
    override val layoutResourceId: Int = R.layout.fragment_two
    private val bottomNavOptionId: Int = R.id.fragmentTwo

    override fun onResume() {
        super.onResume()
        subscribeToNavigationHost()
        updateBottomNavigationSelection(bottomNavOptionId)
    }

    override fun onPause() {
        super.onPause()
        unsubscribeFromNavigationHost()
    }
}