package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.joel.duoclinkayudantia.model.Apunte

@Composable
fun ApuntesScreen(navController: NavController) {


    val demo = listOf(
        Apunte(
            id = 1,
            titulo = "Apunte demo",
            descripcion = "Contenido de prueba",
            url = "",
            autor = "Sistema",
            fecha = "2025-12-14"
        )
    )

    Column {
        Text(text = "Pantalla de Apuntes")

        LazyColumn {
            items(demo) { apunte ->
                Text(apunte.titulo)
            }
        }
    }
}