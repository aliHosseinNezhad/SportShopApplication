package com.gamapp.sportshopapplication.network.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.gamapp.sportshopapplication.AppOld
import com.gamapp.sportshopapplication.model.user.ShoppingBagItem
import com.gamapp.sportshopapplication.model.user.User
import com.gamapp.sportshopapplication.model.user.UserSecondeData
import com.google.gson.GsonBuilder
import java.lang.Exception

class UserDataManager private constructor() {

    companion object {
        var Instance = UserDataManager()
        const val USER_NOT_FOUNT = "user_not_found"
    }

//    private var hasTask: Boolean = false
//    private fun canStartTask() = !hasTask

    enum class TaskInfo {
        INIT, CHANGE, REMOVE, ADD, CLEAR
    }

    inner class Task(var info: TaskInfo, var index: Int?, var isFirst: Boolean) {
        constructor(info: TaskInfo) : this(info, null, true)
        constructor(info: TaskInfo, index: Int) : this(info, index, true)

        fun endTask() {
            isFirst = false
        }
    }

    var task: MutableLiveData<Task> = MutableLiveData()
        private set

    var user: User = User()


    init {
        task.postValue(Task(TaskInfo.INIT))
    }

    fun removeItem(index: Int, param: (result: String, successful: Boolean) -> Unit) {

        try {
            user.let { user ->
                user.secondData.shoppingBag.let {
                    it.items.removeAt(index)
                    setTotals()
                    uploadUserData { result, successful ->
                        if (successful) {
                            task.postValue(Task(TaskInfo.REMOVE, index))
                        }
                        param(result, successful)
                    }
                }
            }
        } catch (e: Exception) {
            param("", false)
        }

    }

    fun setTotals() {
        user.secondData.shoppingBag.items.let { items ->
            var totalCount = 0
            var totalPrice = 0
            for (item in items) {
                totalCount += item.count
                totalPrice += item.count * item.price
            }
            user.secondData.shoppingBag.totalCount = totalCount
            user.secondData.shoppingBag.totalPrice = totalPrice
        }
    }

    fun uploadUserData(param: (result: String, Boolean) -> Unit) {

//            hasTask = true
        if (UserConnection().isCompleteUserLogin()) {
            Backendless.UserService.let {
                it.CurrentUser()?.let { currentUser ->
                    user.let { user ->
                        currentUser.setProperty("name", user.name)
                        currentUser.setProperty("phone", user.phoneNumber)
                        currentUser.setProperty("data", convertToJson(user.secondData))
                        it.update(currentUser, object : AsyncCallback<BackendlessUser> {
                            override fun handleResponse(response: BackendlessUser?) {
                                param("successful", true)
//                                    hasTask = false
                            }

                            override fun handleFault(fault: BackendlessFault?) {
                                param(fault?.message ?: "", false)
//                                    hasTask = false
                            }
                        })

                    }


                }

            }
        }

    }

    fun downloadUserData(param: (result: String, successful: Boolean) -> Unit) {
        if (UserConnection().isCompleteUserLogin()) {
            UserConnection().refreshCurrentUser { successful, result ->
//                Log.i(AppOld.TAG, "downloadUserData: ")
                param(result,successful)
            }
        } else param("", false)

    }


    fun onRefreshCurrentUser() {
        if (UserConnection().isCompleteUserLogin()) {
            Backendless.UserService.let { service ->
                service.CurrentUser()?.let {
                    user.let { user ->
                        user.email = service.CurrentUser().email
                        user.phoneNumber =
                            (service.CurrentUser().getProperty("phone")?.toString())
                                ?: ""
                        user.name =
                            (service.CurrentUser().getProperty("name")?.toString())
                                ?: ""
                        user.secondData =
                            convertFromJson(
                                (service.CurrentUser().getProperty("data")?.toString())
                                    ?: ""
                            )
                        setTotals()
                        task.postValue(Task(TaskInfo.INIT))
                        return
                    }
                }
            }
        }
    }

    fun addNewItemToBasket(
        item: ShoppingBagItem,
        param: (result: String, successful: Boolean) -> Unit
    ) {

        if (UserConnection().isCompleteUserLogin()) {
            user.let { user ->
                user.secondData.shoppingBag.let {
                    it.items.add(item)
                    setTotals()
                    uploadUserData() { result, successful ->
                        if (successful) {
                            task.postValue(Task(TaskInfo.ADD, it.items.lastIndex))
                        }
                        param(result, successful)
                    }

                }
            }
        } else param(USER_NOT_FOUNT, false)


    }

    fun setProductCount(
        index: Int,
        count: Int,
        param: (result: String, successful: Boolean) -> Unit
    ) {
        if (UserConnection().isCompleteUserLogin()) {
            user.let { user ->
                user.secondData.shoppingBag.let {
                    try {
                        it.items[index].count = count
                        setTotals()
                        uploadUserData() { result, successful ->
                            if (successful) {
                                task.postValue(Task(TaskInfo.CHANGE, index))
                            }
                            param(result, successful)
                        }

                    } catch (e: Exception) {
                        param("", false)
                        Log.i(AppOld.TAG, "setProductCount: can not setProduct Count")
                    }
                }
            }

        }

    }


    fun convertToJson(user: UserSecondeData): String {
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        return gson.toJson(user, UserSecondeData::class.java) ?: ""
    }

    fun convertFromJson(json: String): UserSecondeData {
        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation().create()
        return try {
            gson.fromJson(json, UserSecondeData::class.java)
        } catch (e: Exception) {
            UserSecondeData()
        }
    }

    fun clearLogoutData() {
        user = User()
        task.postValue(Task(TaskInfo.CLEAR))
    }

    fun clearUserBasket(param: (result: String, successful: Boolean) -> Unit) {

        if (UserConnection().isCompleteUserLogin()) {
            user.let { user ->
                user.secondData.shoppingBag.let {
                    it.totalPrice = 0
                    it.totalCount = 0
                    it.items = ArrayList()
                    uploadUserData { result, successful ->
                        if (successful) {
                            task.postValue(Task(TaskInfo.CLEAR))
                        }
                        param(result, successful)
                    }
                }
            }

        } else param("", false)

    }

}