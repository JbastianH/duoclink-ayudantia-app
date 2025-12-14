package com.joel.duoclinkayudantia.navigation

sealed class AppRoute(val path: String) {
    object Login : AppRoute("login")
    object Home : AppRoute("home")
    object Apuntes : AppRoute("apuntes")

    object Ayudantias : AppRoute("ayudantias")
    object Perfil : AppRoute("perfil")
    object CrearAyudantia : AppRoute("crear_ayudantia")
    object EditarAyudantia : AppRoute("editar_ayudantia/{ayudantiaId}") {
        fun createRoute(ayudantiaId: String) = "editar_ayudantia/$ayudantiaId"
    }
    object AyudantiasGraph : AppRoute("ayudantias_graph")
}