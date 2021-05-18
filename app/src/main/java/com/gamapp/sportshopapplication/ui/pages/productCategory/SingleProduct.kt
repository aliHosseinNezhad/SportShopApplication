package com.gamapp.sportshopapplication.ui.pages.productCategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gamapp.sportshopapplication.databinding.SingleProductFragmentBinding
import com.gamapp.sportshopapplication.model.products.Product
import com.gamapp.sportshopapplication.model.user.ShoppingBagItem
import com.gamapp.sportshopapplication.network.user.UserConnection
import com.gamapp.sportshopapplication.network.user.UserDataManager
import com.gamapp.sportshopapplication.ui.UIAccessibility
import com.google.android.material.button.MaterialButton
import java.lang.Exception

class SingleProduct(
    var product: Product,
    var adapterPosition: Int,
    var category: String,
) : Fragment() {
    private var _binding: SingleProductFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var addBasketBtn: MaterialButton
    lateinit var productImage: ImageView
    lateinit var productPrice: TextView
    lateinit var productName: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SingleProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setViewsData()
    }

    private fun initView() {
        productImage = binding.productImage
        productName = binding.productName
        productPrice = binding.productPrice
        addBasketBtn = binding.addingBasketBtn
        addBasketBtn.let { it ->
            it.setOnClickListener {
                val bagItem = ShoppingBagItem().apply {
                    category = this@SingleProduct.category
                    this.imageUrl = this@SingleProduct.product.imageUrl
                    this.name = this@SingleProduct.product.name
                    this.price = this@SingleProduct.product.price
                    index = this@SingleProduct.adapterPosition
                    count = 1
                }
                binding.jobWaitingView.visibility = View.VISIBLE
                UserDataManager.Instance.addNewItemToBasket(bagItem) { result, successful ->
                    context?.let { context ->
                        binding.jobWaitingView.visibility = View.GONE

                        if (successful) {
                            Toast.makeText(context, "کالا به سبد خرید اضافه شد ", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            if (!UserConnection().isCompleteUserLogin()) {
                                try {
                                    (context as UIAccessibility).setViewPagerPosition(1)
                                    Toast.makeText(
                                        context,
                                        "لطفا ابتدا وارد شوید",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } catch (e: Exception) {

                                }
                            }

                        }
                    }


                }
            }
        }
    }

    private fun setViewsData() {
        Glide.with(this).load(product.imageUrl).centerInside().into(productImage)
        productPrice.text = product.price.toString()
        productName.text = product.name
    }

}