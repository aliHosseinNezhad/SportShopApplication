package com.gamapp.sportshopapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamapp.sportshopapplication.databinding.BasketItemViewBinding
import com.gamapp.sportshopapplication.model.user.ShoppingBagItem
import com.gamapp.sportshopapplication.network.user.UserDataManager
import com.gamapp.sportshopapplication.ui.customViews.QuantityView
import kotlinx.coroutines.*
import java.lang.Exception

class ShoppingBasketAdapter(var context: Context) :
    RecyclerView.Adapter<ShoppingBasketAdapter.ShoppingBasketViewHolder>() {
    companion object{
        var products: ArrayList<ShoppingBagItem> = ArrayList()
    }

    inner class ShoppingBasketViewHolder(var binding: BasketItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var quantityView: QuantityView = binding.quantityView

        init {
            binding.quantityView.let {
                it.onDeleteItem {
                    var position = adapterPosition
                    waitingProductChange(position)
                    UserDataManager.Instance.removeItem(position) { result, successful ->
                        if (!successful)
                            stopWaitingChange(position)
                    }
                }
                it.onQuantityChange { count, networkResultInterface ->
                    var position = adapterPosition
                    waitingProductChange(position)
                    UserDataManager.Instance.setProductCount(
                        position,
                        count
                    ) { result, successful ->
                        if (!successful)
                            stopWaitingChange(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBasketViewHolder {
        val binding = BasketItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ShoppingBasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingBasketViewHolder, index: Int) {
        var position = index
        products[position].let { item ->
            holder.binding.image.let {
                Glide.with(context).load(item.imageUrl).centerInside().into(it)
            }
            holder.binding.let { view ->
                view.price.text = item.price.toString()
                view.name.text = item.name
            }

            holder.quantityView.let { quantityView ->
                quantityView.setMaxMin(1, 5)
                quantityView.setQuantity(item.count)
                quantityView.setLoading(item.onChanging)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun itemChangeNotify(index: Int) {
        MainScope().launch {
            notifyItemChanged(index)
        }
    }

    private fun waitingProductChange(index: Int) {
        try {
            products[index].onChanging = true
            itemChangeNotify(index)
        } catch (e: Exception) {

        }

    }

    private fun stopWaitingChange(index: Int) {
        try {
            products[index].onChanging = false
            itemChangeNotify(index)
        } catch (e: Exception) {

        }
    }



}