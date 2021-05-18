package com.gamapp.sportshopapplication.model.products

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category {
    @Expose
    @SerializedName("product")
    var products: List<Product> = ArrayList()

    @Expose
    @SerializedName("products")
    var productsString: String = ""

    @Expose
    @SerializedName("image_url")
    var imageUrl: String = ""

    @Expose
    @SerializedName("count")
    var productCount: Int = 0

    @Expose
    @SerializedName("category")
    var category: String = ""

    @Expose
    @SerializedName("category_persian")
    var categoryPersian: String = ""
}