package com.gamapp.sportshopapplication.network.user

import android.text.method.BaseKeyListener
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.gamapp.sportshopapplication.network.connection.NetworkChangeCallbackInterface
import com.gamapp.sportshopapplication.network.connection.NetworkState
import kotlin.math.log

class UserConnection : NetworkChangeCallbackInterface {
    init {
        NetworkState.addToCallBackList(this)
    }

    companion object {
        const val USER_REMOVED_ERROR: String = "3048"
        const val INVALID_EMAIL_OR_PASSWORD_ERROR: String = "3003"
        const val INTERNET_CONNECTION_ERROR = "internet_connection_error"
        const val USER_NOT_LOGIN_ERROR = "user_not_login_error"
        const val MULTIPLE_USER_LOGIN = "3044"
        const val THIS_USER_IS_REGISTERED = "3033"
    }


    private var sendable: Boolean = true

    private var loginParam: ((successful: Boolean, result: String) -> Unit)? = null
    private var refreshLoginParam: ((successful: Boolean, result: String) -> Unit)? = null
    private var registerParam: ((successful: Boolean, result: String) -> Unit)? = null
    private var logoutParam: ((successful: Boolean, result: String) -> Unit)? = null
    private var guestLoginParam: ((successful: Boolean, result: String) -> Unit)? = null

    fun refreshLogin(param: (successful: Boolean, result: String) -> Unit) {
        if (networkConnection()) {
            val id = Backendless.UserService.loggedInUser()
            id?.let {
                if(it.isEmpty()){
                    loginAsGuest(param)
                } else {
                    Backendless.UserService.findById(it,object :AsyncCallback<BackendlessUser>{
                        override fun handleResponse(response: BackendlessUser?) {
                            Backendless.UserService.setCurrentUser(response)
                            sendResponse(true,"successful",param)
                        }

                        override fun handleFault(fault: BackendlessFault?) {
                            loginAsGuest(param)
                        }

                    })
                }
            }?:run {
                loginAsGuest(param)
            }
        } else {
            sendResponse(false, INTERNET_CONNECTION_ERROR, param)
        }
    }

    fun login(user: BackendlessUser, param: (successful: Boolean, result: String) -> Unit) {
        loginParam = param
        if (networkConnection()) {
            Backendless.UserService.login(
                user.email,
                user.password,
                object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser?) {
                        Backendless.UserService.setCurrentUser(response)
                        sendResponse(true, "", param)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        sendResponse(false, fault?.code ?: "", param)
                    }
                },
                true
            )
        } else {
            sendResponse(false, INTERNET_CONNECTION_ERROR, param)
        }
    }

    fun registerAndLogin(
        user: BackendlessUser,
        param: (successful: Boolean, result: String) -> Unit
    ) {
        if (networkConnection()) {
            Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser> {
                override fun handleResponse(response: BackendlessUser?) {
                    response?.let {
                        login(it, param)
                        return
                    }
                    param(false, "")
                }

                override fun handleFault(fault: BackendlessFault?) {
                    sendResponse(false, fault?.code ?: "", param)
                }

            })
        } else {
            sendResponse(false, INTERNET_CONNECTION_ERROR, param)
        }

    }

    fun logout(param: (successful: Boolean, result: String) -> Unit) {
        if (networkConnection()) {
            Backendless.UserService.logout(object : AsyncCallback<Void> {
                override fun handleResponse(response: Void?) {
                    UserDataManager.Instance.clearLogoutData()
                    sendResponse(true, "", param)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    sendResponse(false, fault?.message ?: "", param)
                }

            })
        } else {
            sendResponse(false, INTERNET_CONNECTION_ERROR, param)
        }

    }

    fun loginAsGuest(param: (successful: Boolean, result: String) -> Unit) {
        guestLoginParam = param
        if (networkConnection()) {
            Backendless.UserService.loginAsGuest(object : AsyncCallback<BackendlessUser> {
                override fun handleResponse(response: BackendlessUser?) {
                    Backendless.UserService.setCurrentUser(response)
                    sendResponse(true, "", param)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    sendResponse(false, fault?.message ?: "", param)
                }
            }, true)
        } else {
            sendResponse(false, INTERNET_CONNECTION_ERROR, param)
        }

    }

    private fun sendResponse(
        successful: Boolean,
        result: String,
        param: (successful: Boolean, result: String) -> Unit
    ) {
        if (sendable) {
            param(successful, result)
        }
        refreshCurrentUser { successful, result -> }
    }


    override fun onNetworkConnectionChange(connection: Boolean) {
        sendable = connection
        if (!connection) {
            loginParam?.let { it(false, INTERNET_CONNECTION_ERROR) }
            refreshLoginParam?.let { it(false, INTERNET_CONNECTION_ERROR) }
            logoutParam?.let { it(false, INTERNET_CONNECTION_ERROR) }
            registerParam?.let { it(false, INTERNET_CONNECTION_ERROR) }
        }
    }

    fun networkConnection(): Boolean {
        return NetworkState.isConnected
    }

    fun isCompleteUserLogin() =
        !(Backendless.UserService.CurrentUser() == null
                || Backendless.UserService.CurrentUser().isEmpty
                || Backendless.UserService.CurrentUser().getProperty("userStatus") == "GUEST")

    fun refreshCurrentUser(param: (successful: Boolean, result: String) -> Unit) {
        if (isCompleteUserLogin()) {
            Backendless.UserService.findById(Backendless.UserService.loggedInUser(),
                object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser?) {
                        Backendless.UserService.setCurrentUser(response)
                        UserDataManager.Instance.onRefreshCurrentUser()
                        param(true,"successful")
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        param(false, fault?.message ?: "")
                    }
                })
        } else param(false, USER_NOT_LOGIN_ERROR)
    }

}