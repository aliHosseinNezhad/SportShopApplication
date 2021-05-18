package com.gamapp.sportshopapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gamapp.sportshopapplication.AppOld
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.ActivityLoadingBinding
import com.gamapp.sportshopapplication.network.user.UserConnection
import com.gamapp.sportshopapplication.network.user.UserDataManager
import com.squareup.picasso.Picasso
//import eightbitlab.com.blurview.RenderScriptBlur
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var loginScope: CoroutineScope
    private lateinit var binding: ActivityLoadingBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        startLogin()
    }

    private fun startLogin() {
        startLoginUi()
        UserConnection().refreshLogin { successful, result ->
            if (successful) {
                startMainActivity()
            } else {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                Log.i(AppOld.TAG, "$result ")
                startErrorUi()
            }
        }
    }


    private fun startLoginUi() {
        stopErrorUi()
        binding.loadingView.visibility = View.VISIBLE
    }

    private fun stopLoginUi() {
        binding.loadingView.visibility = View.GONE
    }

    private fun startErrorUi() {
        stopLoginUi()
        binding.errorView.visibility = View.VISIBLE
    }

    private fun stopErrorUi() {
        binding.errorView.visibility = View.GONE
    }


    private fun setUpViews() {
//        Picasso.get().load(R.drawable.sport_background_per_4).fit().centerCrop().into(binding.backgroundImage)
        binding.reloadBtn.setOnClickListener {
            startLogin()
        }

        val decorView: View = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
//        binding.blurView3.setupWith(rootView)
//            .setFrameClearDrawable(windowBackground)
//            .setBlurAlgorithm(RenderScriptBlur(this))
//            .setBlurRadius(3f)
//            .setBlurAutoUpdate(true)
    }


    private fun startMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }

    override fun onBackPressed() {}

}