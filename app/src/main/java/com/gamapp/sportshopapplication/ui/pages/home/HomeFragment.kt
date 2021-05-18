package com.gamapp.sportshopapplication.ui.pages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.HomeFragmentBinding
import com.gamapp.sportshopapplication.ui.UIAccessibility

class HomeFragment(var uiAccessibility: UIAccessibility) : Fragment(R.layout.home_fragment) {
    val binding: HomeFragmentBinding
        get() = _binding!!
    var _binding: HomeFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        setUpViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            Glide.with(it).load(R.drawable.sport_shop_banner).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(binding.specialSaleImage)
        }
    }

    private fun setUpViews() {
        binding.categoryBtn.setOnClickListener {
            uiAccessibility.setViewPagerPosition(2)
        }
        binding.sportShopBanner.setOnClickListener {
            Toast.makeText(context, "به فروشگاه ورزشی خوش آمدید", Toast.LENGTH_LONG).show()
        }
        binding.shoppingBasketBtn.setOnClickListener {
            uiAccessibility.setViewPagerPosition(1)
        }
        binding.specialSaleBtn.setOnClickListener {
            startSpecialSaleFragment()
        }
    }

    private fun startSpecialSaleFragment() {
        var fragment = SpecialSaleFragment()
        activity?.let {
            it.supportFragmentManager.beginTransaction().add(binding.frameLayout.id, fragment)
                .addToBackStack(null).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}