package com.gamapp.sportshopapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.CategoryItemViewHolderBinding
import com.gamapp.sportshopapplication.model.products.Category
import com.gamapp.sportshopapplication.ui.pages.productCategory.ProductCategory
import com.gamapp.sportshopapplication.ui.pages.productCategory.Products

class CategoriesAdapter(var fragment: ProductCategory, var categories: List<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {

    var context = fragment.context

    inner class CategoriesHolder(binding: CategoryItemViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        var image: ImageView = binding.image
        var category: TextView = binding.category
        var productCount: TextView = binding.productCount

        init {
            binding.categoryView.setOnClickListener {
                fragment.activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)?.add(R.id.fragment_layout,
                    Products(categories[adapterPosition])
                )?.commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        var view =
            CategoryItemViewHolderBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoriesHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        var category = categories[position]


        Glide.with(context!!)
            .load(category.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.loading_spinner)
            .into(holder.image)

        holder.category.text = category.category
        holder.productCount.text = context?.getString(R.string.product_count, category.productCount)
    }

    override fun getItemCount(): Int = categories.size

}