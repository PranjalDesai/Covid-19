package com.pranjaldesai.coronavirustracker.helper

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import com.pranjaldesai.coronavirustracker.extension.isConnectedToNetwork

class ConnectivityMonitor(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        subscribeToNetworkChanges()
    }

    override fun onInactive() {
        super.onInactive()
        unsubscribeFromNetworkChanges()
    }

    private fun subscribeToNetworkChanges() {
        with(connectivityManager) {
            postValue(isConnectedToNetwork())
            registerDefaultNetworkCallback(networkCallback)
        }
    }

    private fun unsubscribeFromNetworkChanges() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}