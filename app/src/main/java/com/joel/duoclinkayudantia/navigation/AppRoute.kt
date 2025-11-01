package com.joel.duoclinkayudantia.navigation

sealed class AppRoute(val path: String) {
    data object Root : AppRoute("root")
    data object Login : AppRoute("login")
    data object Home : AppRoute("home")
}