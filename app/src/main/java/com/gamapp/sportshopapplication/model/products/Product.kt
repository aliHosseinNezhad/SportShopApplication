package com.gamapp.sportshopapplication.model.products

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {
    @Expose
    var name:String=""

    @Expose
    @SerializedName("objectId")
    var id:String=""

    @Expose
    var price:Int=0

    @Expose
    @SerializedName("real_price")
    var realPrice:Int=0

    @Expose
    var category:String=""

    @Expose
    var description:String=""

    @Expose
    @SerializedName("image_url")
    var imageUrl:String=""
}