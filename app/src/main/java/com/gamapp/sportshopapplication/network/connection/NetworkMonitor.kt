package com.gamapp.sportshopapplication.network.connection

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.gamapp.sportshopapplication.AppOld.Companion.TAG

class NetworkMonitor @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
constructor(private val application: Application) {

    fun startNetworkCallback() {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        /**Check if version code is greater than API 24*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(networkCallback)
        } else {
            cm.registerNetworkCallback(
                builder.build(), networkCallback
            )
        }
    }

    fun stopNetworkCallback() {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onUnavailable() {
            super.onUnavailable()
            Log.i(TAG, "onUnavailable: ")
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.i(TAG, "onAvailable: ")
            NetworkState.isConnected = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.i(TAG, "onLost: ")
            NetworkState.isConnected = false
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.i(TAG, "onLosing: ")
        }
    }

    /**Deprecated Code*/
    fun oldNetwork() {
        fun isNetworkAvailable(): Boolean {
            val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }
}