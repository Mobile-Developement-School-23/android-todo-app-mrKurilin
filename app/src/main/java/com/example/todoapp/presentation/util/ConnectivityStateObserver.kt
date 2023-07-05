package com.example.todoapp.presentation.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Observes the network connectivity state and provides updates to clients.
 *
 * This class monitors the network state using the ConnectivityManager and emits updates
 * about the current network connectivity state through a Flow. Clients can collect updates
 * from the [networkConnectivityState] to stay informed about changes in the network state.
 */
class ConnectivityStateObserver @Inject constructor(
    connectivityManager: ConnectivityManager
) {

    private val _networkConnectivityState = MutableStateFlow(NetworkConnectivityState.LOST)
    val networkConnectivityState = _networkConnectivityState.asStateFlow()

    init {
        setInitNetworkConnectivityState(connectivityManager)
        registerDefaultNetworkCallback(connectivityManager)
    }

    private fun registerDefaultNetworkCallback(connectivityManager: ConnectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(
            ConnectivityManagerNetworkCallback()
        )
    }

    private fun setInitNetworkConnectivityState(connectivityManager: ConnectivityManager) {
        val actNw = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isNetworkAvailable = when {
            actNw == null -> false
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
        if (isNetworkAvailable) {
            _networkConnectivityState.update { NetworkConnectivityState.AVAILABLE }
        }
    }

    inner class ConnectivityManagerNetworkCallback : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _networkConnectivityState.update { NetworkConnectivityState.AVAILABLE }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _networkConnectivityState.update { NetworkConnectivityState.LOST }
        }
    }
}