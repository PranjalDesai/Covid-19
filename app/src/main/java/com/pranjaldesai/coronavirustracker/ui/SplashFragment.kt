package com.pranjaldesai.coronavirustracker.ui

import androidx.navigation.fragment.findNavController
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.databinding.FragmentSplashBinding
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment

class SplashFragment : CoreFragment<FragmentSplashBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_splash

    override fun bindData() {
        super.bindData()
        navigateIntoApp()

    }

    private fun navigateIntoApp() {
        if (findNavController().currentDestination?.id == R.id.splashFragment) {
            findNavController().navigate(R.id.covidDetail)
        }
    }

}