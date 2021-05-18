package com.gamapp.sportshopapplication.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gamapp.sportshopapplication.ui.pages.customer.ManageCustomerPage
import com.gamapp.sportshopapplication.ui.pages.productCategory.ProductCategory
import com.gamapp.sportshopapplication.ui.pages.ShoppingBagFragment
import com.gamapp.sportshopapplication.ui.pages.home.HomeFragment

class ShopViewPagerAdapter(
    var context: Context,
    var fragmentManger: FragmentManager,
    var uiAccessibility: UIAccessibility
) : FragmentPagerAdapter(
    fragmentManger, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {


//    companion object : SingletonHolder<ShopViewPagerAdapter,
//            Context,
//            FragmentManager,
//            ViewPager>(::ShopViewPagerAdapter)


    private var manageCustomerPage: ManageCustomerPage =
        ManageCustomerPage( uiAccessibility)
    private var shoppingBagFragment = ShoppingBagFragment( uiAccessibility)

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                manageCustomerPage.resfresh()
                manageCustomerPage
            }
            1 -> {
//                shoppingBagFragment.refresh()
                shoppingBagFragment
            }
            2 -> {
                ProductCategory(uiAccessibility)
            }
            else -> HomeFragment(uiAccessibility)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "حساب من"
            1 -> "سبد خرید"
            2 -> "دسته بندی ها"
            3 -> "خانه"
            else -> ""
        }
    }

//    fun refresh(position: Int) {
//        if (position == 1)
////            shoppingBagFragment.refresh()
//    }


}

