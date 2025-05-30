package com.example.sermanna.components

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Search : Screens("search")
    object Messages : Screens("messages")
    object Profile : Screens("profile")
}