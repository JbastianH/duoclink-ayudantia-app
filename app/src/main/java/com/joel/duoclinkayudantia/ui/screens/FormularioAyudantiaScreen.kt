package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.viewmodel.AyudantiaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAyudantiaScreen(
    navController: NavController,
    vm: AyudantiaViewModel,
    ayudantiaId: Int? = null
) {
    val formState by vm.formState.collectAsState()
    val isEditing = ayudantiaId != null
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        if (isEditing) {
            vm.cargarAyudantiaParaEditar(ayudantiaId!!)
        } else {
            vm.resetFormState()
        }
    }

    LaunchedEffect(formState.success) {
        if (formState.success) {
            navController.popBackStack()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val date = formatter.format(Date(millis))
                        vm.onFormValueChange(dia = date)
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Ayudantía" else "Crear Ayudantía") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ... El resto del formulario es idéntico al de CrearAyudantiaScreen ...
            OutlinedTextField(
                value = formState.publicadoPor,
                onValueChange = { },
                label = { Text("Publicado Por") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )

            OutlinedTextField(
                value = formState.tema,
                onValueChange = { vm.onFormValueChange(tema = it) },
                label = { Text("Tema") },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.temaError != null,
                supportingText = { formState.temaError?.let { Text(it) } }
            )

            OutlinedTextField(
                value = formState.lugar,
                onValueChange = { vm.onFormValueChange(lugar = it) },
                label = { Text("Lugar") },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.lugarError != null,
                supportingText = { formState.lugarError?.let { Text(it) } }
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = formState.dia,
                    onValueChange = { },
                    label = { Text("Día") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true },
                    isError = formState.diaError != null,
                    supportingText = { formState.diaError?.let { Text(it) } },
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedTextField(
                        value = formState.horaInput,
                        onValueChange = { if (it.length <= 2) vm.onFormValueChange(horaInput = it) },
                        label = { Text("HH") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = formState.horaError != null
                    )
                    Text(":", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 8.dp))
                    OutlinedTextField(
                        value = formState.minutoInput,
                        onValueChange = { if (it.length <= 2) vm.onFormValueChange(minutoInput = it) },
                        label = { Text("MM") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = formState.horaError != null
                    )
                }
            }
            // Muestra el mensaje de error para la hora debajo de los campos
            if (formState.horaError != null) {
                Text(
                    text = formState.horaError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                )
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = formState.cupos,
                    onValueChange = { vm.onFormValueChange(cupos = it) },
                    label = { Text("Cupos") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formState.cuposError != null,
                    supportingText = { formState.cuposError?.let { Text(it) } }
                )
                OutlinedTextField(
                    value = formState.duracion,
                    onValueChange = { vm.onFormValueChange(duracion = it) },
                    label = { Text("Duración") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formState.duracionError != null,
                    supportingText = { formState.duracionError?.let { Text(it) } },
                    suffix = { Text("min") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { vm.guardarAyudantia() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Ayudantía")
            }
        }
    }
}