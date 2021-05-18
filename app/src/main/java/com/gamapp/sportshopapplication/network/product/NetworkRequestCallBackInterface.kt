package com.gamapp.sportshopapplication.network.product

interface NetworkRequestCallBackInterface<T> {
    fun onRequestSuccessful(data:T? , message:String)
    fun onError(error:String,type:String)
}