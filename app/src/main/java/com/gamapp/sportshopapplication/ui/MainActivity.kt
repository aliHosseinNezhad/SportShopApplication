package com.gamapp.sportshopapplication.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.*
import com.gamapp.sportshopapplication.network.user.UserDataManager
import com.google.android.material.tabs.TabLayout
import io.github.inflationx.viewpump.ViewPumpContextWrapper


class MainActivity : AppCompatActivity(), UIAccessibility {
    private lateinit var homeTabView: HomeTabViewBinding

    private lateinit var categoryTabView: CategoryTabViewBinding

    private lateinit var basketTabView: BasketTabViewBinding

    private lateinit var clientTabView: ClientPageTabViewBinding


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private lateinit var binding: ActivityMainBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setUpViewPager()

        UserDataManager.Instance.task.observe(this) {
            setBasketTabViewCount(UserDataManager.Instance.user.secondData.shoppingBag.totalCount)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (viewPager.currentItem == 3) {
                finishAffinity()
            } else {
                viewPager.setCurrentItem(3, false)
            }
        } else
            super.onBackPressed()
    }

    private fun setUpViewPager() {
        var shopAdapter = ShopViewPagerAdapter(this, supportFragmentManager, this)
        viewPager.adapter = shopAdapter
        tabLayout.setupWithViewPager(viewPager)
        setViewPagerPosition(3)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                shopAdapter.refresh(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        setTabLayoutTabs()
    }

    private fun initViews() {
        tabLayout = binding.tabLayout
        tabLayout.tabRippleColor = null
        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.primaryLightColor))
        viewPager = binding.viewPager
    }

    private fun setTabLayoutTabs() {

        clientTabView = ClientPageTabViewBinding.inflate(layoutInflater)
        tabLayout.getTabAt(0)?.customView = clientTabView.root
        clientTabView.notificationIcon.visibility = View.INVISIBLE


        basketTabView = BasketTabViewBinding.inflate(layoutInflater)
        tabLayout.getTabAt(1)?.customView = basketTabView.root
        basketTabView.selectedItemCount.text = "12"


        categoryTabView = CategoryTabViewBinding.inflate(layoutInflater)
        tabLayout.getTabAt(2)?.customView = categoryTabView.root


        homeTabView = HomeTabViewBinding.inflate(layoutInflater)
        tabLayout.getTabAt(3)?.customView = homeTabView.root

    }

    override fun setViewPagerPosition(position: Int) {
        viewPager.setCurrentItem(position, false)
    }

    override fun setBasketTabViewCount(count: Int) {
        if (count == 0) {
            basketTabView.selectedItemCount.visibility = View.GONE
            return
        }
        basketTabView.selectedItemCount.visibility = View.VISIBLE
        basketTabView.selectedItemCount.text = count.toString()
    }
}

interface UIAccessibility {
    fun setViewPagerPosition(position: Int)

    fun setBasketTabViewCount(count: Int)
}
