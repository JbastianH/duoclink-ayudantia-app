package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                    Text("Publicar")
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
                            onUnirseClick = { vm.unirse(ayudantia) }
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
    onUnirseClick: () -> Unit
) {
    val isFull = ayudantia.inscritos >= ayudantia.cupo
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = ayudantia.materia,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Por: ${ayudantia.autor.nombre}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Día: ${ayudantia.dia}", style = MaterialTheme.typography.bodySmall)
                Text("Horario: ${ayudantia.horario}", style = MaterialTheme.typography.bodySmall)
            }
            Text("Lugar: ${ayudantia.lugar}", style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("Cupos: ${ayudantia.inscritos} / ${ayudantia.cupo}") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (isFull) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                    )
                )
                
                Button(
                    onClick = onUnirseClick,
                    enabled = !isFull
                ) {
                    Text(if (isFull) "Sin Cupos" else "Unirme")
                }
            }
        }
    }
}