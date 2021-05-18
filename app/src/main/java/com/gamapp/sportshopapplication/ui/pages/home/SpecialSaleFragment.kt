package com.gamapp.sportshopapplication.ui.pages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.SpecialSaleFragmentBinding
import com.gamapp.sportshopapplication.model.products.Product
import com.gamapp.sportshopapplication.network.product.ProductDataManger
import com.gamapp.sportshopapplication.ui.adapter.ProductsAdapter

class SpecialSaleFragment : Fragment(R.layout.special_sale_fragment) {

    private fun showSpecialProduct() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = productAdapter
    }

    private var productAdapter: ProductsAdapter? = null
    val binding: SpecialSaleFragmentBinding get() = _binding!!
    var _binding: SpecialSaleFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SpecialSaleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.loadingBar.visibility = View.VISIBLE
        ProductDataManger.Instance.getProductsByCategory("boat", download = false) { result: String, successful: Boolean, list: List<Product> ->
            if (successful) {
                productAdapter =
                    ProductsAdapter(this, list, "boat", getString(R.string.frame_layout))
                showSpecialProduct()
            }
            binding.loadingBar.visibility = View.GONE
        }
    }
}