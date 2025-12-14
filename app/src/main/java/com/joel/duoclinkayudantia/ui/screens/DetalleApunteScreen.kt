package com.joel.duoclinkayudantia.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.model.Apunte
import com.joel.duoclinkayudantia.navigation.AppRoute
import com.joel.duoclinkayudantia.viewmodel.ApuntesViewModel
import com.joel.duoclinkayudantia.ui.theme.DuocBlue
import com.joel.duoclinkayudantia.ui.theme.DuocYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleApunteScreen(
    navController: NavController,
    vm: ApuntesViewModel
) {
    val apunte by vm.apunteSeleccionado.collectAsState()
    val isLoading by vm.isLoadingList.collectAsState()
    val currentUserUid = vm.currentUserUid
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Apunte") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (apunte != null && apunte?.userId == currentUserUid) {
                        IconButton(onClick = {
                            navController.navigate(AppRoute.EditarApunte.createRoute(apunte!!.id))
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            vm.eliminarApunte(apunte!!.id)
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (apunte != null) {
                val currentApunte = apunte!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = currentApunte.titulo,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (!currentApunte.tags.isNullOrEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            currentApunte.tags.forEach { tag ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(tag) }
                                )
                            }
                        }
                    }

                    if (currentApunte.type == "link" && !currentApunte.link.isNullOrBlank()) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentApunte.link))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DuocYellow,
                                contentColor = DuocBlue
                            )
                        ) {
                            Icon(Icons.Default.Link, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Abrir Enlace")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (!currentApunte.descripcion.isNullOrBlank()) {
                            Text(
                                text = "Descripción:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentApunte.descripcion,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        Text(
                            text = currentApunte.descripcion ?: "",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                Text(
                    text = "No se encontró el apunte",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
