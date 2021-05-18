package com.gamapp.sportshopapplication.ui.pages.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.backendless.Backendless
import com.gamapp.sportshopapplication.MyLocationOnMap
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.MainCustomerPageFragmentBinding
import com.gamapp.sportshopapplication.network.user.UserConnection
import com.gamapp.sportshopapplication.network.user.UserDataManager
import com.gamapp.sportshopapplication.ui.UIAccessibility
import com.gamapp.sportshopapplication.ui.dialog.LogoutDialog
import ir.tapsell.sdk.bannerads.TapsellBannerType
import ir.tapsell.sdk.nativeads.TapsellNativeBannerViewManager


class MainCustomerPage(
    var manageCustomerPage: ManageCustomerPage,
    var uiAccessibility: UIAccessibility
) :
    Fragment(R.layout.main_customer_page_fragment), PopupMenu.OnMenuItemClickListener {
    private val ZONE_ID_NATIVE: String = "606da393e07c8c00011aefdb"
    private var nativeBannerViewManager: TapsellNativeBannerViewManager? = null
    private lateinit var basketBtn: FrameLayout
    private lateinit var settingBtn: ImageView
    private var _binding: MainCustomerPageFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainCustomerPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        UserDataManager.Instance.task.observe(viewLifecycleOwner) {
            val count = UserDataManager.Instance.user.secondData.shoppingBag.totalCount
            if (count == 0) {
                binding.basketView.selectedItemCount.visibility = View.GONE
                return@observe
            }
            binding.basketView.selectedItemCount.visibility = View.VISIBLE
            binding.basketView.selectedItemCount.text = count.toString()
        }
    }

    private fun setUpViews() {

        binding.adContainer.loadAd(context, ZONE_ID_NATIVE, TapsellBannerType.BANNER_320x100)
        binding.startMapBtn.setOnClickListener {
            startShopMap()
        }

        binding.buyHistoryBtn.setOnClickListener{
            Toast.makeText(context,getString(R.string.demo_reason),Toast.LENGTH_LONG).show()
        }
        binding.canceledBuyBtn.setOnClickListener{
            Toast.makeText(context,getString(R.string.demo_reason),Toast.LENGTH_LONG).show()
        }
        binding.userAccountInfoBtn.setOnClickListener {
            Toast.makeText(context,getString(R.string.demo_reason),Toast.LENGTH_LONG).show()
        }

        settingBtn = binding.settingBtn
        settingBtn.setOnClickListener {
            showSettingPopUpMenu(settingBtn)
        }
        basketBtn = binding.basketBtn
        basketBtn.setOnClickListener {
            uiAccessibility.setViewPagerPosition(1)
        }
        UserDataManager.Instance.task.observe(viewLifecycleOwner){

            binding.userName.text = UserDataManager.Instance.user.name
        }
    }

    private fun startShopMap() {
        activity?.let {
            val fragment = MyLocationOnMap()
            it.supportFragmentManager.beginTransaction()
                .add(binding.googleMapFragment.id, fragment)
                .addToBackStack(null).commit()
        }
    }


    private fun showSettingPopUpMenu(view: View) {
        var popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.setting_popup_menu)
        try {
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        } catch (e: Exception) {
        } finally {
            popupMenu.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.logout -> {
                    onLogoutDialog()
                }
                R.id.profile -> {
                    Toast.makeText(context,getString(R.string.demo_reason),Toast.LENGTH_LONG).show()
                }
                R.id.address ->{
                    Toast.makeText(context,getString(R.string.demo_reason),Toast.LENGTH_LONG).show()
                }
            }
        }
        return false
    }

    private fun onLogoutDialog() {
        fragmentManager?.let { fragmentManager ->
            LogoutDialog(context!!).let { dialog ->
                dialog.onResultListener {
                    if(it){
                        logout()
                    }
                }
                dialog.show() }
        }
    }

    private fun logout() {
        binding.logoutWaitingView.visibility = View.VISIBLE
        UserConnection().logout { successful: Boolean, result: String ->
            if (successful) {
                manageCustomerPage.logout()
            }
            binding.logoutWaitingView.visibility = View.VISIBLE
        }
    }
}