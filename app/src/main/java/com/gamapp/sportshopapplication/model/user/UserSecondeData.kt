package com.gamapp.sportshopapplication.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserSecondeData constructor(
    @Expose
    var address: Address = Address(),
    @Expose
    @SerializedName("shopping_bag")
    var shoppingBag: ShoppingBag = ShoppingBag()
)