package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.R
import com.joel.duoclinkayudantia.navigation.AppRoute
import com.joel.duoclinkayudantia.ui.theme.DuocBlue
import com.joel.duoclinkayudantia.ui.theme.DuocWhite
import com.joel.duoclinkayudantia.ui.theme.DuocYellow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    // Estados de los campos.
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Interacciones para animar el botón.
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Colores animados del botón.
    val targetContainer = if (isPressed) DuocYellow else DuocBlue
    val targetContent = if (isPressed) DuocBlue else DuocWhite
    val animatedContainer by animateColorAsState(targetContainer)
    val animatedContent by animateColorAsState(targetContent)

    // Snackbar para mensajes de validación.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Scaffold para alojar snackbar y contenido.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(DuocWhite)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo institucional.
            Image(
                painter = painterResource(id = R.drawable.dllogo),
                contentDescription = "DuocLink Logo",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(2.dp))

            // Campo Usuario.
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario", color = DuocBlue) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DuocBlue,
                    unfocusedBorderColor = DuocBlue,
                    focusedLabelColor = DuocBlue,
                    unfocusedLabelColor = DuocBlue,
                    cursorColor = DuocBlue,
                    focusedTextColor = DuocBlue,
                    unfocusedTextColor = DuocBlue
                )
            )

            Spacer(Modifier.height(16.dp))

            // Campo Contraseña.
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = DuocBlue) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DuocBlue,
                    unfocusedBorderColor = DuocBlue,
                    focusedLabelColor = DuocBlue,
                    unfocusedLabelColor = DuocBlue,
                    cursorColor = DuocBlue,
                    focusedTextColor = DuocBlue,
                    unfocusedTextColor = DuocBlue
                )
            )

            Spacer(Modifier.height(32.dp))

            // Botón de inicio de sesión con validación y navegación segura.
            Button(
                onClick = {
                    when {
                        username.isBlank() && password.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Debe ingresar usuario y contraseña") }
                        }
                        username.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Debe ingresar el usuario") }
                        }
                        password.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Debe ingresar la contraseña") }
                        }
                        else -> {
                            // Validación simple (mock): credenciales fijas.
                            val ok = (username == "admin" && password == "1234")
                            if (ok) {
                                navController.navigate(AppRoute.Home.path) {
                                    // Elimina Login del back stack para no volver con Atrás.
                                    popUpTo(AppRoute.Login.path) { inclusive = true }
                                }
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("Usuario o contraseña incorrectos") }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = animatedContainer,
                    contentColor = animatedContent
                )
            ) {
                Text("Iniciar sesión")
            }
        }
    }
}