package com.gamapp.sportshopapplication.network.connection

import kotlin.properties.Delegates

object NetworkState {
    private var list: ArrayList<NetworkChangeCallbackInterface> = ArrayList()
    var isConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        for(receiver in list){
           receiver.onNetworkConnectionChange(newValue)
        }
    }
    fun addToCallBackList(input : NetworkChangeCallbackInterface){
        list.add(input)
    }
}