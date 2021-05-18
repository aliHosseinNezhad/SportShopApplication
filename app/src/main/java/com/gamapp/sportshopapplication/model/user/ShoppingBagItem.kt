package com.gamapp.sportshopapplication.model.user

import com.google.gson.annotations.Expose

data class ShoppingBagItem constructor(
    @Expose(serialize = true, deserialize = true)
    var index: Int = 0,
    @Expose(serialize = true, deserialize = true)
    var category: String = "",
    @Expose(serialize = true, deserialize = true)
    var count: Int = 0,
    @Expose(serialize = true, deserialize = true)
    var imageUrl:String = "" ,
    @Expose(serialize = true, deserialize = true)
    var name:String = "" ,
    @Expose(serialize = true, deserialize = true)
    var price:Int = 0 ,
    var onChanging: Boolean = false
)
