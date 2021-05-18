package com.gamapp.sportshopapplication.ui.pages.productCategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.ProductsFragmentBinding
import com.gamapp.sportshopapplication.model.products.Category
import com.gamapp.sportshopapplication.model.products.Product
import com.gamapp.sportshopapplication.ui.adapter.ProductsAdapter
import com.github.ybq.android.spinkit.SpinKitView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Products(var category: Category) : Fragment(R.layout.products_fragment) {
    private var TAG = "tag345345"
    private var _binding: ProductsFragmentBinding? = null
    private lateinit var loadingBar: SpinKitView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentView: FrameLayout



    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        showProducts()
//        requestCategories(category)
    }

    private fun showProducts() {
        var productList = getProductList(category.productsString)
        if (productList != null) {
            recyclerView.adapter = ProductsAdapter(this,productList,category.category,getString(R.string.frame_layout))
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun getProductList(products: String): List<Product>? {
        var gson = Gson()
        var type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson<List<Product>>(products,type)
    }


    private fun initView() {
        loadingBar = binding.loadingBar
        recyclerView = binding.recyclerView
        fragmentView = binding.fragmentView
    }


//    private fun requestCategories(category: String) {
//        loadingBar.visibility = View.VISIBLE
//        NetworkCallBack.INSTANCE.getProductsByCategory(category, object :
//            NetworkRequestCallBackInterface<Category> {
//            override fun onRequestSuccessful(data: Category?, message: String) {
//                Log.i(TAG, "onRequestSuccessful: " + message)
//                showCategories(data)
//                loadingBar.visibility = View.GONE
//            }
//
//            override fun onError(error: String, type: String) {
//                Log.i(TAG, "onError: " + type + " " + error)
//            }
//        })
//    }


}