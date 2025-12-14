package com.joel.duoclinkayudantia.ui.screens

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.viewmodel.AyudantiaViewModel
import java.util.Calendar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAyudantiaScreen(
    navController: NavController,
    vm: AyudantiaViewModel
) {
    val formState by vm.formState.collectAsState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Date Picker Logic
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            vm.onDiaChange(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time Picker Logic
    fun showTimePicker(onTimeSelected: (String) -> Unit) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                onTimeSelected(time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

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
                title = { Text("Publicar Ayudantía") },
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
                // Materia
                OutlinedTextField(
                    value = formState.materia,
                    onValueChange = { vm.onMateriaChange(it) },
                    label = { Text("Materia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Cupo
                OutlinedTextField(
                    value = formState.cupo,
                    onValueChange = { vm.onCupoChange(it) },
                    label = { Text("Cupo (máx 40)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Horarios
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = formState.horarioInicio,
                        onValueChange = {},
                        label = { Text("Inicio") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker { vm.onHorarioInicioChange(it) } }) {
                                Icon(Icons.Default.AccessTime, "Seleccionar hora inicio")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = formState.horarioFin,
                        onValueChange = {},
                        label = { Text("Fin") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker { vm.onHorarioFinChange(it) } }) {
                                Icon(Icons.Default.AccessTime, "Seleccionar hora fin")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Día
                OutlinedTextField(
                    value = formState.dia,
                    onValueChange = {},
                    label = { Text("Día") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, "Seleccionar día")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Lugar
                OutlinedTextField(
                    value = formState.lugar,
                    onValueChange = { vm.onLugarChange(it) },
                    label = { Text("Lugar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (formState.error != null) {
                    Text(formState.error!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = { vm.publicarAyudantia() },
                    enabled = !formState.isLoading && !formState.success,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Publicar Ayudantía")
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
                            Text("¡Publicada!", modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
        }
    }
}
