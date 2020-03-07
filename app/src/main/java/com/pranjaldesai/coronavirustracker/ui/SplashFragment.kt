package com.pranjaldesai.coronavirustracker.ui

import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.databinding.FragmentSplashBinding
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment

class SplashFragment : CoreFragment<FragmentSplashBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_splash

    override fun bindData() {
        super.bindData()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        launchOnMain {
            navigateIntoApp()
        }

    }

    private fun navigateIntoApp() {
        if (findNavController().currentDestination?.id == R.id.splashFragment) {
            findNavController().navigate(R.id.fragmentOne)
        }
    }

//    private fun setUpCrashlyticsInfo() {
//        Crashlytics.setUserName(sharedPreferences.displayName)
//        Crashlytics.setUserIdentifier(sharedPreferences.userId)
//        Crashlytics.setUserEmail(sharedPreferences.userEmail)
//    }
}