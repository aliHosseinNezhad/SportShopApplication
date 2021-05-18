package com.gamapp.sportshopapplication.network.product

import com.gamapp.sportshopapplication.model.products.Category
import com.gamapp.sportshopapplication.model.products.Product
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

class ProductDataManger private constructor() {
    companion object {
        val Instance = ProductDataManger()
    }
    
    private var categories: List<Category>? = null

    fun getAllProduct(download: Boolean = true, param: (String, Boolean, List<Category>) -> Unit) {
        if (download || categories == null) {
            NetworkCallBack.INSTANCE.getCategoriesList { result, successful, categories ->
                if (successful) {
                    for (category in categories) {
                        category.let {
                            it.products = convertToProducts(it.productsString)
                        }
                    }
                }
                param(result, successful, categories)
            }
        } else
            categories?.let {
                param("", true, it)
            }

    }

    fun getProductsByCategory(
        categoryName: String,
        download: Boolean = true,
        param: (String, Boolean, List<Product>) -> Unit) {

        







        getAllProduct(download) { result: String, successful: Boolean, categories: List<Category> ->
            if (successful) {
                for (category in categories) {
                    if (category.category == categoryName) {
                        category.let {
                            it.products = convertToProducts(it.productsString)
                            param(result, successful, it.products)
                            return@getAllProduct
                        }
                    }
                }
            }
            param(result, successful, ArrayList())
        }
    }

    fun getProductByIndexAndCategory(
        categoryName: String,
        index: Int,
        download: Boolean = true,
        param: (String, Boolean, Product) -> Unit
    ) {
        getProductsByCategory(
            categoryName,
            download
        ) { result: String, successful: Boolean, products: List<Product> ->
            if (successful) {
                for (i in products.indices) {
                    if (i == index) {
                        param(result, successful, products[i])
                        return@getProductsByCategory
                    }
                }

            }
            param(result, successful, Product())
        }

    }

    private fun convertToProducts(products: String): List<Product> {
        var gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        var type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(products, type)
    }
}