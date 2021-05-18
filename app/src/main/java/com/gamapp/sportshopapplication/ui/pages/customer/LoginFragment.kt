package com.gamapp.sportshopapplication.ui.pages.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.backendless.BackendlessUser
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.LoginFragmentBinding
import com.gamapp.sportshopapplication.network.user.UserConnection
import eightbitlab.com.blurview.RenderScriptBlur

//import eightbitlab.com.blurview.RenderScriptBlur



class LoginFragment(private var manageCustomerPage: ManageCustomerPage) :
    Fragment(R.layout.login_fragment) {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {


//        val radius = 3f
//
//        val decorView: View = activity!!.window.decorView
//        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
//        val windowBackground = decorView.background
//        binding.blurView.setupWith(rootView)
//            .setFrameClearDrawable(windowBackground)
//            .setBlurAlgorithm(RenderScriptBlur(context))
//            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
//        binding.blueView2.setupWith(rootView)
//            .setFrameClearDrawable(windowBackground)
//            .setBlurAlgorithm(RenderScriptBlur(context))
//            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)



        binding.loginBtn.setOnClickListener {
            if (binding.email.text.toString().isNotEmpty() && binding.password.text.toString()
                    .isNotEmpty()
            ) {
                loginUser(binding.email.text.toString(), binding.password.text.toString())
            } else {

            }
        }
//        activity?.let { activity->
//            Picasso.get().load(R.drawable.sport_background_per_4).fit().centerCrop().into(binding.backgroundImage)
//        }

        binding.registerBtn.setOnClickListener {
            manageCustomerPage.requestFragment(RegisterFragment(manageCustomerPage))
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.loginWaitingView.visibility = View.VISIBLE
        UserConnection().let { userConnection ->
            userConnection.login(BackendlessUser().apply {
                this.email = email
                this.password = password
            }) { successful, result ->
                if (successful) {
                    binding.loginWaitingView.visibility = View.GONE
                    Toast.makeText(
                        context,
                        getString(R.string.successfull_login_text),
                        Toast.LENGTH_SHORT
                    ).show()
                    manageCustomerPage.startMainCustomerFragment()
                } else {
                    when (result) {
                        UserConnection.INVALID_EMAIL_OR_PASSWORD_ERROR -> {
                            Toast.makeText(
                                context,
                                getString(R.string.wrong_email_or_password),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginWaitingView.visibility = View.GONE
                        }
                        UserConnection.USER_REMOVED_ERROR -> {
                            Toast.makeText(
                                context,
                                "",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginWaitingView.visibility = View.GONE
                        }
                        UserConnection.INTERNET_CONNECTION_ERROR -> {
                            Toast.makeText(
                                context,
                                "احتمالا این حساب کاربری از دسترس خارج شده است",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginWaitingView.visibility = View.GONE
                        }
                        UserConnection.MULTIPLE_USER_LOGIN ->{
                            Toast.makeText(
                                context,
                                " شما فقط روی تعداد محدودی حساب می توانید لاگین کنید ",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginWaitingView.visibility = View.GONE
                        }
                        else -> {
                            Toast.makeText(context, "code: $result ", Toast.LENGTH_SHORT).show()
                            binding.loginWaitingView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun removeFragments() {
        var fragments = activity?.supportFragmentManager?.fragments
        var size: Int = fragments?.size ?: 0
        for (i in 0 until size - 1) {
            fragments?.get(i)
                ?.let { activity?.supportFragmentManager?.beginTransaction()?.remove(it) }
        }
    }
}



