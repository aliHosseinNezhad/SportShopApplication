package com.gamapp.sportshopapplication.network.product

import com.gamapp.sportshopapplication.App
import com.gamapp.sportshopapplication.model.products.Category
import retrofit2.Call
import retrofit2.http.GET

interface NetworkMethodInterface {
    companion object{
        var BASE_URL= App.DATA_URL
    }
//    @GET("services/sport_shop/product_by_category")
//    fun getProductsByCategory(@Query("category") category: String): Call<Category>

    @GET("category")
    fun getCategoryList():Call<List<Category>>
}