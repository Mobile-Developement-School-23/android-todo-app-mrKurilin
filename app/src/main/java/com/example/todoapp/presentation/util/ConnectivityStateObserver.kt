package com.example.todoapp.presentation.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ConnectivityStateObserver @Inject constructor(
    connectivityManager: ConnectivityManager
) {

    private val _networkConnectivityState = MutableStateFlow(NetworkConnectivityState.LOST)
    val networkConnectivityState = _networkConnectivityState.asStateFlow()

    init {
        if (isNetworkAvailable(connectivityManager)) {
            _networkConnectivityState.update {
                NetworkConnectivityState.AVAILABLE
            }
        }

        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    _networkConnectivityState.update {
                        NetworkConnectivityState.AVAILABLE
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    _networkConnectivityState.update {
                        NetworkConnectivityState.LOST
                    }
                }
            }
        )
    }

    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val actNw = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            actNw == null -> false
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}