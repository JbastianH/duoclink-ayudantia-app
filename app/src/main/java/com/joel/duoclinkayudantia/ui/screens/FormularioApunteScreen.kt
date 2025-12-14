package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.viewmodel.ApuntesViewModel
import com.joel.duoclinkayudantia.ui.theme.DuocBlue
import com.joel.duoclinkayudantia.ui.theme.DuocYellow
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioApunteScreen(
    navController: NavController,
    vm: ApuntesViewModel
) {
    val formState by vm.formState.collectAsState()

    LaunchedEffect(formState.success) {
        if (formState.success) {
            delay(1500)
            vm.resetSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (formState.isEditing) "Editar Apunte" else "Publicar Apunte") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TabRow(selectedTabIndex = if (formState.tipo == "link") 1 else 0) {
                    Tab(
                        selected = formState.tipo == "text",
                        onClick = { vm.onTipoChange("text") },
                        text = { Text("Texto") }
                    )
                    Tab(
                        selected = formState.tipo == "link",
                        onClick = { vm.onTipoChange("link") },
                        text = { Text("Enlace") }
                    )
                }

                OutlinedTextField(
                    value = formState.titulo,
                    onValueChange = { vm.onTituloChange(it) },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (formState.tipo == "text") {
                    OutlinedTextField(
                        value = formState.descripcion,
                        onValueChange = { vm.onDescripcionChange(it) },
                        label = { Text("Contenido *") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        maxLines = 10
                    )
                }

                if (formState.tipo == "link") {
                    OutlinedTextField(
                        value = formState.url,
                        onValueChange = { vm.onUrlChange(it) },
                        label = { Text("URL *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("https://...") }
                    )

                    OutlinedTextField(
                        value = formState.descripcion,
                        onValueChange = { vm.onDescripcionChange(it) },
                        label = { Text("Descripción (Opcional)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 5
                    )
                }

                OutlinedTextField(
                    value = formState.tags,
                    onValueChange = { vm.onTagsChange(it) },
                    label = { Text("Tags (separados por coma)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("ej: java, examen, resumen") }
                )

                if (formState.error != null) {
                    Text(formState.error!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = { vm.publicarApunte() },
                    enabled = !formState.isLoading && !formState.success,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DuocYellow,
                        contentColor = DuocBlue
                    )
                ) {
                    Text(if (formState.isEditing) "Guardar Cambios" else "Publicar Apunte")
                }
            }

            if (formState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (formState.success) {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 6.dp) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                            Text(if (formState.isEditing) "¡Actualizado!" else "¡Publicado!", modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
        }
    }
}
