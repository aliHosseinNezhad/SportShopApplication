package com.gamapp.sportshopapplication.model.user

import com.google.gson.annotations.Expose

data class ShoppingBag constructor(
    @Expose
    var items: ArrayList<ShoppingBagItem> = ArrayList(),
    @Expose
    var totalCount: Int = 0,
    @Expose
    var totalPrice:Int = 0
)