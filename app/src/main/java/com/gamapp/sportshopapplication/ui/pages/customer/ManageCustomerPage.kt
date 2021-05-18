package com.gamapp.sportshopapplication.ui.pages.customer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.network.user.UserConnection
import com.gamapp.sportshopapplication.ui.UIAccessibility

class ManageCustomerPage(var uiAccessibility: UIAccessibility) :
    Fragment(R.layout.client_page_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    fun start(){
        if (UserConnection().isCompleteUserLogin()) {
            startMainCustomerFragment()
        } else {
            startLoginFragment()
        }
    }

    fun startMainCustomerFragment() {
        activity?.let { activity ->
            val fragment = MainCustomerPage(this, uiAccessibility)
            this.fragmentManager?.let {
                var count = it.backStackEntryCount
                for (i in 0 until count) {
                    it.popBackStack()
                }
            }
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, fragment).commit()
        }
    }

    private fun startLoginFragment() {
        activity?.let {
            val fragment = LoginFragment(this)
            it.supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, fragment)
                .commit()
        }

    }

    fun requestFragment(fragment: Fragment) {
        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        }

    }

    fun resfresh() {

    }

    fun logout() {
        this.fragmentManager?.let {
            var count = it.backStackEntryCount
            for (i in 0 until count-1) {
                it.popBackStack()
            }
            start()
        }
    }
}