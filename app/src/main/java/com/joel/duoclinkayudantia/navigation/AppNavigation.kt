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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.joel.duoclinkayudantia.ui.screens.*

@Composable
fun AppNavigation() {
    // Se crea y recuerda un controlador de navegación para manejar las rutas de la aplicación.
    val navController = rememberNavController()
    // Se obtiene la entrada actual del back stack para conocer la pantalla activa.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    // Se define una lista de rutas en las cuales se debe mostrar la barra inferior de navegación.
    val showBottomBar = currentDest?.route in listOf(
        AppRoute.Home.path, AppRoute.Ayudantias.path, AppRoute.Perfil.path
    )

    // Se utiliza un Scaffold como estructura base que permite incluir la barra inferior y el contenido principal.
    Scaffold(
        bottomBar = {
            // Solo se muestra la barra inferior si la ruta actual pertenece a las rutas definidas.
            if (showBottomBar) {
                NavigationBar {
                    // Se recorre la lista de ítems de navegación para generar los botones de la barra inferior.
                    bottomItems.forEach { item ->
                        // Se verifica si el destino actual coincide con la ruta del ítem.
                        val selected = currentDest.isOn(item.route)
                        // Se crea un elemento de navegación para cada opción del menú inferior.
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                // Al presionar un ítem, se navega hacia su ruta correspondiente.
                                navController.navigate(item.route) {
                                    // Limpia el back stack hasta Home antes de navegar
                                    popUpTo(AppRoute.Home.path) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            // Se define el ícono y la descripción accesible del elemento.
                            icon = { Icon(item.icon, contentDescription = item.cd) },
                            // Se asigna la etiqueta visible del ítem.
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { inner ->
        // Se define el contenedor de navegación principal (NavHost) que administra las pantallas de la app.
        NavHost(
            navController = navController,
            startDestination = AppRoute.Login.path, // Pantalla inicial.
            modifier = Modifier.padding(inner)
        ) {
            // Cada composable representa una pantalla dentro del grafo de navegación.
            composable(AppRoute.Login.path) { LoginScreen(navController) }
            composable(AppRoute.Home.path) { HomeScreen(navController) }
            composable(AppRoute.Ayudantias.path) { AyudantiasScreen() }
            composable(AppRoute.Perfil.path) { PerfilScreen() }
        composable(route = AppRoute.Home.path) {
            HomeScreen(navController)
        }
    }
}

/* ---- Definición de la estructura de cada ítem del menú inferior ---- */
private data class BottomItem(
    val route: String, // Ruta asociada al ítem.
    val label: String, // Texto visible debajo del ícono.
    val cd: String,    // Descripción accesible del ítem.
    val icon: androidx.compose.ui.graphics.vector.ImageVector // Ícono del ítem.
)

/* ---- Lista de ítems visibles en la barra inferior ---- */
private val bottomItems = listOf(
    BottomItem(AppRoute.Home.path, "Inicio", "Ir a inicio", Icons.Filled.Home),
    BottomItem(AppRoute.Ayudantias.path, "Ayudantías", "Ir a ayudantías", Icons.Filled.School),
    BottomItem(AppRoute.Perfil.path, "Perfil", "Ir a perfil", Icons.Filled.Person),
)

/* ---- Función auxiliar para determinar si el destino actual coincide con una ruta dada ---- */
private fun NavDestination?.isOn(route: String) =
    this?.hierarchy?.any { it.route == route } == true