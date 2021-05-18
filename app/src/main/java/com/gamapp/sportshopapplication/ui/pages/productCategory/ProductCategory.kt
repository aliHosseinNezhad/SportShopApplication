package com.gamapp.sportshopapplication.ui.pages.productCategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.ProductCategoryFragmentBinding
import com.gamapp.sportshopapplication.model.products.Category
import com.gamapp.sportshopapplication.network.ErrorType
import com.gamapp.sportshopapplication.network.product.NetworkCallBack
import com.gamapp.sportshopapplication.network.product.NetworkRequestCallBackInterface
import com.gamapp.sportshopapplication.ui.UIAccessibility
import com.gamapp.sportshopapplication.ui.adapter.CategoriesAdapter
import com.github.ybq.android.spinkit.SpinKitView


class ProductCategory(context: UIAccessibility) : Fragment(R.layout.product_category_fragment) {
    private var TAG = "tag345345"
    private var _binding: ProductCategoryFragmentBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    lateinit var recyclerView: RecyclerView
    lateinit var loadingBar: SpinKitView
    lateinit var serverErrorView: View
    lateinit var connectionErrorView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductCategoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        requestCategories()


        binding.swipeRefresh.setOnRefreshListener {
            binding.recyclerView.visibility = View.VISIBLE
            recreateFragment()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        recyclerView = binding.recyclerView
        loadingBar = binding.loadingBar
        serverErrorView = binding.serverError.root
        connectionErrorView = binding.connectionError.root
    }

    private fun recreateFragment() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.detach(this)
            ?.attach(this)
            ?.commit()
    }


    private fun requestCategories() {
        loadingBar.visibility = View.VISIBLE
        NetworkCallBack.INSTANCE.getCategoriesList(object :
            NetworkRequestCallBackInterface<List<Category>> {
            override fun onRequestSuccessful(data: List<Category>?, message: String) {
//                Log.i(TAG, "onRequestSuccessful: " + message)
//                Log.i(TAG, "onRequestSuccessful: " + data?.get(0)?.imageUrl)
                showCategories(data)
                loadingBar.visibility = View.GONE
            }

            override fun onError(error: String, type: String) {
//                Log.i(TAG, "onError: " + type + " " + error)
                loadingBar.visibility = View.GONE
                if (type == ErrorType.CONNECTION_ERROR) {
                    connectionErrorView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else if (type == ErrorType.SERVER_ERROR) {
                    serverErrorView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }

            }

        })
    }

    private fun showCategories(data: List<Category>?) {
        if (context != null && data != null) {
            recyclerView.adapter = CategoriesAdapter(this, data)
            recyclerView.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

//    private fun requestProductsByCategory(category: String) {
//        NetworkCallBack.INSTANCE.getProductsByCategory("boat",
//            object : NetworkRequestCallBackInterface<Category> {
//                override fun onRequestSuccessful(data: Category?, message: String) {
////                    Log.i(TAG, "onRequestSuccessful: " + message)
////                    showCategories(data)
//                }
//
//                override fun onError(error: String, type: String) {
////                    Log.i(TAG, "onError: " + type + " " + error)
//                }
//            })
//    }
}