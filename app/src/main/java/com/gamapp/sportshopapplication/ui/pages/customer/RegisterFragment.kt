package com.gamapp.sportshopapplication.ui.pages.customer

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.RegisterFragmentBinding
import com.gamapp.sportshopapplication.network.user.UserConnection
import com.gamapp.sportshopapplication.utils.ImageUtils
import com.gamapp.sportshopapplication.utils.ImageUtils.Companion.resizeImage
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso


class RegisterFragment(var manageCustomerPage: ManageCustomerPage) :
    Fragment(R.layout.register_fragment) {
    private lateinit var registerBtn: MaterialButton
    private lateinit var phoneNumber: TextInputEditText
    private lateinit var repeatPassword: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var name: TextInputEditText
    private val binding get() = _binding!!
    private var _binding: RegisterFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        name = binding.name
        email = binding.email
        password = binding.password
        repeatPassword = binding.repeatPassword
        phoneNumber = binding.phoneNo
        registerBtn = binding.registerBtn
//        activity?.let { activity->
//            Picasso.get().load(R.drawable.sport_background_per_4).fit().centerCrop().into(binding.backgroundImage)
////           Glide.with(activity).load(R.drawable.sport_background_low).optionalCenterInside().into(binding.backgroundImage)
//        }
        registerBtn.setOnClickListener {
            startUserRegister()
        }

        email.addTextChangedListener {
            it?.let {
                if (it.isEmpty() || it.toString() == "") {
                    email.error = "please enter email address"
                } else {
                    email.error = null
                }
            }
        }
        password.addTextChangedListener {
            it?.let {
                if (it.isEmpty() || it.toString() == "") {
                    password.error = "please enter your password"
                } else {
                    password.error = null
                }
            }
        }
        repeatPassword.addTextChangedListener {
            it?.let {
                if (it.isEmpty() || it.toString() == "") {
                    repeatPassword.error = "please repeat your password"
                } else {
                    repeatPassword.error = null
                }
            }
        }

    }


    private fun startUserRegister() {
        if (password.text.toString().isNotEmpty()
            && email.text.toString().isNotEmpty()
            && repeatPassword.text.toString().isNotEmpty()
        ) {
            var user = BackendlessUser()
            user.email = email.text.toString()
            user.password = password.text.toString()
            user.setProperty("name", name.text?.toString() ?: "")
            binding.loginWaitingView.visibility = View.VISIBLE
            UserConnection().let { userConnection ->
                userConnection.registerAndLogin(user) { successful, result ->
                    if (successful) {
                        Toast.makeText(
                            context,
                            getString(R.string.successful_register_text),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.loginWaitingView.visibility = View.GONE
                        manageCustomerPage.startMainCustomerFragment()
                    } else {
                        binding.loginWaitingView.visibility = View.GONE
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
                            UserConnection.THIS_USER_IS_REGISTERED ->{
                                Toast.makeText(
                                    context,
                                    "کسی با این ایمیل قبلا ثبت نام کرده است.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.loginWaitingView.visibility = View.GONE
                            }
                            else -> {
                                Toast.makeText(context, result, Toast.LENGTH_SHORT)
                                    .show()
                                binding.loginWaitingView.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

    }


}