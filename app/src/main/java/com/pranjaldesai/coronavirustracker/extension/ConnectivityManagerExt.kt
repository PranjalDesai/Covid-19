package com.pranjaldesai.coronavirustracker.extension

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge

fun ConnectivityManager.isConnectedToNetwork(): Boolean {
    return try {
        val network = activeNetwork ?: return false
        val activeNetwork = getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    } catch (exception: Exception) {
        loge(exception)
        false
    }
}

fun ConnectivityManager.isConnectedToCellular(): Boolean {
    return try {
        val network = activeNetwork ?: return false
        val activeNetwork = getNetworkCapabilities(network) ?: return false
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    } catch (exception: Exception) {
        false
    }
}