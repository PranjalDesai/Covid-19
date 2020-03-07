package com.pranjaldesai.coronavirustracker.ui

import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.databinding.FragmentOneBinding
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment

class FragmentOne : CoreFragment<FragmentOneBinding>(), IPrimaryFragment {
    override val layoutResourceId: Int = R.layout.fragment_one
    private val bottomNavOptionId: Int = R.id.fragmentOne

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