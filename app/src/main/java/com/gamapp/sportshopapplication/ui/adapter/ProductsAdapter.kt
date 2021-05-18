package com.gamapp.sportshopapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.ProductItemViewHolderBinding
import com.gamapp.sportshopapplication.model.products.Product
import com.gamapp.sportshopapplication.ui.pages.productCategory.Products
import com.gamapp.sportshopapplication.ui.pages.productCategory.SingleProduct
import com.google.android.material.textview.MaterialTextView

class ProductsAdapter(var fragment: Fragment, var products: List<Product>, var category: String , var tag:String ) :
    RecyclerView.Adapter<ProductsAdapter.ProductsHolder>() {
    var context = fragment.context
    var activity = fragment.activity

    inner class ProductsHolder(itemView: ProductItemViewHolderBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var image: ImageView = itemView.image
        var name: MaterialTextView = itemView.name
        var price: TextView = itemView.price
        var productView = itemView.productView

        init {
            productView.setOnClickListener {
                val frameLayout = fragment.view?.findViewWithTag<FrameLayout>(tag)
                    activity?.let { activity ->
                        frameLayout?.let {
                            activity.supportFragmentManager
                                .beginTransaction()
                                .add(it.id, SingleProduct(products[adapterPosition],adapterPosition,category))
                                .addToBackStack(null)
                                .commit()
                        }
                    }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        var view = ProductItemViewHolderBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductsHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        var product = products[position]

        Glide.with(context!!)
            .load(product.imageUrl)
            .centerInside()
            .placeholder(R.drawable.loading_spinner)
            .into(holder.image)
        holder.name.text = product.name
        holder.price.text = product.price.toString()

    }

    override fun getItemCount(): Int = products.size
}