package com.joel.duoclinkayudantia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joel.duoclinkayudantia.ui.screens.HomeScreen
import com.joel.duoclinkayudantia.ui.screens.LoginScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Login.path,
        route = AppRoute.Root.path
    ) {
        composable(route = AppRoute.Login.path) {
            LoginScreen(navController)
        }
        composable(route = AppRoute.Home.path) {
            HomeScreen()
        }
    }
}