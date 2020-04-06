package com.pranjaldesai.coronavirustracker.ui

import androidx.navigation.findNavController
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.helper.ConnectivityMonitor
import com.pranjaldesai.coronavirustracker.ui.shared.FMNavigationHostActivity
import org.koin.android.ext.android.inject
import java.util.Arrays.asList

class MainActivity : FMNavigationHostActivity(), IBottomNavMenuUpdater {

    override val connectivityMonitor: ConnectivityMonitor by inject()
    override val primaryFragmentResIdList: List<Int> = asList(
        R.id.covidDetail,
        R.id.covidMap,
        R.id.covidTip
    )

    override fun updateBottomNav(menu: Int) {
        binding.bottomNavigationView.menu.clear()
        binding.bottomNavigationView.inflateMenu(menu)
    }

    override fun setupNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { selectedOption ->
            subscribedFragment?.let {
                val destinationID = it.navigateTo(selectedOption.itemId)
                findNavController(R.id.nav_host_fragment).navigate(destinationID)
            }
            true
        }
    }

}