package com.joel.duoclinkayudantia.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.joel.duoclinkayudantia.ui.screens.AyudantiasScreen
import com.joel.duoclinkayudantia.ui.screens.FormularioAyudantiaScreen
import com.joel.duoclinkayudantia.ui.screens.HomeScreen
import com.joel.duoclinkayudantia.ui.screens.LoginScreen
import com.joel.duoclinkayudantia.ui.screens.PerfilScreen
import com.joel.duoclinkayudantia.viewmodel.AyudantiaViewModel
import androidx.compose.runtime.remember

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val showBottomBar = currentDestination?.route != AppRoute.Login.path

            if (showBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination.isOn(item.route),
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.cd) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Login.path,
            modifier = Modifier.padding(inner)
        ) {
            composable(AppRoute.Login.path) { LoginScreen(navController) }
            composable(AppRoute.Home.path) { HomeScreen(navController) }
            ayudantiasGraph(navController)
            composable(AppRoute.Perfil.path) { PerfilScreen(navController) }
        }
    }
}

fun NavGraphBuilder.ayudantiasGraph(navController: NavHostController) {
    navigation(
        startDestination = AppRoute.Ayudantias.path,
        route = AppRoute.AyudantiasGraph.path
    ) {
        composable(AppRoute.Ayudantias.path) { navBackStackEntry ->
            val backStackEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppRoute.AyudantiasGraph.path)
            }
            val viewModel: AyudantiaViewModel = viewModel(backStackEntry)
            AyudantiasScreen(navController, viewModel)
        }
        composable(AppRoute.CrearAyudantia.path) { navBackStackEntry ->
            val backStackEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppRoute.AyudantiasGraph.path)
            }
            val viewModel: AyudantiaViewModel = viewModel(backStackEntry)
            FormularioAyudantiaScreen(navController, viewModel)
        }
        /*
        composable(
            route = AppRoute.EditarAyudantia.path,
            arguments = listOf(navArgument("ayudantiaId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val backStackEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppRoute.AyudantiasGraph.path)
            }
            val viewModel: AyudantiaViewModel = viewModel(backStackEntry)
            val ayudantiaId = navBackStackEntry.arguments?.getString("ayudantiaId")
            // FormularioAyudantiaScreen(navController, viewModel, ayudantiaId) // TODO: Habilitar edición
        }
        */
    }
}

private data class BottomItem(
    val route: String,
    val label: String,
    val cd: String,
    val icon: ImageVector
)

private val bottomItems = listOf(
    BottomItem(AppRoute.Home.path, "Inicio", "Ir a inicio", Icons.Filled.Home),
    BottomItem(AppRoute.AyudantiasGraph.path, "Ayudantías", "Ir a ayudantías", Icons.Filled.School),
    BottomItem(AppRoute.Perfil.path, "Perfil", "Ir a perfil", Icons.Filled.Person),
)

private fun NavDestination?.isOn(route: String) =
    this?.hierarchy?.any { it.route == route } == true