package com.gamapp.sportshopapplication.model.user

data class User constructor(
    var email: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var secondData: UserSecondeData = UserSecondeData()
)