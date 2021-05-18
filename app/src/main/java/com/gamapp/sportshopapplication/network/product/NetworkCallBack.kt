package com.gamapp.sportshopapplication.network.product

import com.gamapp.sportshopapplication.model.products.Category
import com.gamapp.sportshopapplication.network.ErrorType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkCallBack private constructor() {
    private var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NetworkMethodInterface.BASE_URL)
        .build()
    private var methodInterface: NetworkMethodInterface =
        retrofit.create(NetworkMethodInterface::class.java)

    companion object {
        var INSTANCE: NetworkCallBack = NetworkCallBack()
    }

    fun getCategoriesList(param: (result: String, successful: Boolean, data: List<Category>) -> Unit) {
        getCategoriesList(object : NetworkRequestCallBackInterface<List<Category>> {
            override fun onRequestSuccessful(data: List<Category>?, message: String) {
                param("", true, data ?: ArrayList())
            }

            override fun onError(error: String, type: String) {
                param(type, false, ArrayList())
            }
        })
    }

    fun getCategoriesList(callBack: NetworkRequestCallBackInterface<List<Category>>) {
        var call = methodInterface.getCategoryList()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    callBack.onRequestSuccessful(response.body(), response.message())
                } else {
                    callBack.onError(response.message(), ErrorType.SERVER_ERROR)
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                callBack.onError(t.message.toString(), ErrorType.CONNECTION_ERROR)
            }

        })
    }

//    fun getProductsByCategory(
//        category: String,
//        callBack: NetworkRequestCallBackInterface<Category>
//    ) {
//        var call = methodInterface.getProductsByCategory(category)
//        call.enqueue(object : Callback<Category>{
//            override fun onResponse(
//                call: Call<Category>,
//                response: Response<Category>
//            ) {
//                if(response.isSuccessful){
//                    callBack.onRequestSuccessful(response.body(),response.message())
//                }else{
//                    callBack.onError(response.message(), ErrorType.SERVER_ERROR)
//                }
//            }
//
//            override fun onFailure(call: Call<Category>, t: Throwable) {
//                callBack.onError(t.message.toString(), ErrorType.CONNECTION_ERROR)
//            }
//
//        })
//    }


}
