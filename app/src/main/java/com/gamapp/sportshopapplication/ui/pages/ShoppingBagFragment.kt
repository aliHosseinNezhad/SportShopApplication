package com.gamapp.sportshopapplication.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.ShoppingBagFragmentBinding
import com.gamapp.sportshopapplication.model.user.ShoppingBagItem
import com.gamapp.sportshopapplication.network.user.UserConnection
import com.gamapp.sportshopapplication.network.user.UserDataManager
import com.gamapp.sportshopapplication.network.user.UserDataManager.TaskInfo.*
import com.gamapp.sportshopapplication.ui.UIAccessibility
import com.gamapp.sportshopapplication.ui.adapter.ShoppingBasketAdapter
import com.gamapp.sportshopapplication.ui.customViews.CustomLinearLayout

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ShoppingBagFragment(var uiAccessibility: UIAccessibility) :
    Fragment(R.layout.shopping_bag_fragment) {

    var TAG = "shoppingBasket1"

    var viewFragment: View? = null
    private var _binding: ShoppingBagFragmentBinding? = null
    private val binding get() = _binding!!
    private var shoppingBasketAdapter: ShoppingBasketAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShoppingBagFragmentBinding.inflate(inflater, container, false)
        Log.i(TAG, "onCreateView: ")
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    fun removeItem(index: Int) {
        ShoppingBasketAdapter.products.removeAt(index)
    }


    fun changeItem(index: Int, newCount: Int) {
        ShoppingBasketAdapter.products[index].count = newCount
        ShoppingBasketAdapter.products[index].onChanging = false
    }


    fun setItems(basketItem: java.util.ArrayList<ShoppingBagItem>) {
        ShoppingBasketAdapter.products = basketItem
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        setUpViews()
        viewFragment = view
        context?.let {
            shoppingBasketAdapter = ShoppingBasketAdapter(it)
        }
        UserDataManager.Instance.task.observe(viewLifecycleOwner) { task ->
            if (task.isFirst) {
                task.endTask()
            } else
                task.info = INIT
            UserDataManager.Instance.user.secondData.shoppingBag.let { bag ->
                var items = bag.copy().items
                UserDataManager.Instance.user.secondData.shoppingBag.totalPrice.let {
                    binding.totalPrice.text = it.toString()
                }
                when (task.info) {

                    ADD -> {
                        ShoppingBasketAdapter.products.add(items[task.index!!].copy())
                        shoppingBasketAdapter?.notifyItemInserted(ShoppingBasketAdapter.products.lastIndex)
                    }
                    CHANGE -> {
//                        Toast.makeText(context,"change",Toast.LENGTH_SHORT).show()
                        changeItem(task.index!!, items[task.index!!].count)
                        shoppingBasketAdapter?.notifyItemChanged(task.index!!)
                    }
                    REMOVE -> {
                        Log.i(TAG, "onCreateView: remove ${task.index!!}")
                        removeItem(task.index!!)
                        shoppingBasketAdapter?.notifyItemRemoved(task.index!!)
                    }
                    INIT -> {
                        val copy = ArrayList<ShoppingBagItem>()
                        for (item in items) {
                            copy.add(item.copy())
                        }
                        setItems(copy)
                        shoppingBasketAdapter?.notifyDataSetChanged()
                    }
                    CLEAR -> {
                        ShoppingBasketAdapter.products = ArrayList()
                        shoppingBasketAdapter?.notifyDataSetChanged()
                    }
                }
            }
            if (UserDataManager.Instance.user.secondData.shoppingBag.items.size == 0) {
//                Toast.makeText(context, "basket empty", Toast.LENGTH_SHORT).show()
                showEmptyBasket()
            } else {
                showRecyclerView()
            }

//            Toast.makeText(context, "observed!", Toast.LENGTH_SHORT).show()


        }
        start()
    }

    private fun setUpViews() {
        binding.basketLayout.setOnRefreshListener {
            UserDataManager.Instance.downloadUserData { result, successful ->
//                Toast.makeText(context, result + "$successful",Toast.LENGTH_LONG).show()
                context?.let { binding.basketLayout.isRefreshing = false }
            }
        }

        binding.clearBasket.setOnClickListener {
            binding.jobWaitingView.visibility = View.VISIBLE
            UserDataManager.Instance.clearUserBasket { result, successful ->

                binding.jobWaitingView.visibility = View.GONE
            }
        }
        binding.finishSaleBtn.setOnClickListener {
            binding.jobWaitingView.visibility = View.VISIBLE
            UserDataManager.Instance.clearUserBasket { result, successful ->
                if (successful) {
                    Toast.makeText(context, "خرید شما نهایی شد", Toast.LENGTH_SHORT).show()
                }
                binding.jobWaitingView.visibility = View.GONE
            }
        }
        binding.startUserPanelBtn.setOnClickListener {
            uiAccessibility.setViewPagerPosition(0)
        }
    }

    private fun start() {
        if (UserConnection().isCompleteUserLogin()) {
            startBasketUI()

        } else {
            startLoginAdviceUI()
        }
    }

    private fun startLoginAdviceUI() {
        binding.basketLayout.visibility = View.GONE
        binding.loginBasketLayout.visibility = View.VISIBLE
    }

    private fun startBasketUI() {
        binding.basketLayout.visibility = View.VISIBLE
        binding.loginBasketLayout.visibility = View.GONE
        startRecyclerView()
    }


    private fun showEmptyBasket() {
//        Toast.makeText(context, "im try to visible empty basket", Toast.LENGTH_SHORT).show()
        binding.emptyBasketView.visibility = View.VISIBLE
        binding.saleLayout.visibility = View.GONE
        binding.totalPriceLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.totalPriceLayout.visibility = View.VISIBLE
        binding.saleLayout.visibility = View.VISIBLE
        binding.emptyBasketView.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun startRecyclerView() {
        MainScope().launch {
            binding.recyclerView.let {
                context?.let { context ->
                    it.adapter = shoppingBasketAdapter
                    it.layoutManager =
                        CustomLinearLayout(context, LinearLayoutManager.VERTICAL, false)
                }
            }
        }

    }


}