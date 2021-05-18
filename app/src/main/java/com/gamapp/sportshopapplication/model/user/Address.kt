package com.gamapp.sportshopapplication.model.user

data class Address constructor(
    var country: String = "iran",
    var state: String = "",
    var city: String = "",
    var street: String = ""
)