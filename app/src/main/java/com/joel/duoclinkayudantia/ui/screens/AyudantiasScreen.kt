package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.navigation.AppRoute
import com.joel.duoclinkayudantia.model.Ayudantia
import com.joel.duoclinkayudantia.viewmodel.AyudantiaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudantiasScreen(
    navController: NavController,
    vm: AyudantiaViewModel
) {
    val ayudantias by vm.ayudantias.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var ayudantiaParaEliminar by remember { mutableStateOf<Ayudantia?>(null) }

    if (showDialog && ayudantiaParaEliminar != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar la ayudantía sobre '${ayudantiaParaEliminar!!.tema}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        vm.eliminarAyudantia(ayudantiaParaEliminar!!)
                        ayudantiaParaEliminar = null
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Ayudantías Disponibles",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { navController.navigate(AppRoute.CrearAyudantia.path) },
                ) {
                    Text("Publicar Ayudantía")
                }
            }

            if (ayudantias.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay ayudantías disponibles.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ayudantias) { ayudantia ->
                        AyudantiaCard(
                            ayudantia = ayudantia,
                            onDeleteClick = {
                                ayudantiaParaEliminar = ayudantia
                                showDialog = true
                            },
                            onEditClick = {
                                navController.navigate(AppRoute.EditarAyudantia.createRoute(ayudantia.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AyudantiaCard(
    ayudantia: Ayudantia,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = ayudantia.tema,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Publicado por: ${ayudantia.publicadoPor}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip("Lugar: ${ayudantia.lugar}")
                InfoChip("Día: ${ayudantia.dia}")
                InfoChip("Hora: ${ayudantia.hora}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip("Cupos: ${ayudantia.cupos}")
                InfoChip("Duración: ${ayudantia.duracion} min")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .padding(vertical = 4.dp)
    )
}