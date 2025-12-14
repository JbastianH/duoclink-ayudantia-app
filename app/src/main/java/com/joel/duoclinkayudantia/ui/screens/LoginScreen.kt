package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.joel.duoclinkayudantia.R
import com.joel.duoclinkayudantia.navigation.AppRoute
import com.joel.duoclinkayudantia.ui.theme.DuocBlue
import com.joel.duoclinkayudantia.ui.theme.DuocWhite
import com.joel.duoclinkayudantia.ui.theme.DuocYellow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetContainer = if (isPressed) DuocYellow else DuocBlue
    val targetContent = if (isPressed) DuocBlue else DuocWhite
    val animatedContainer by animateColorAsState(targetContainer)
    val animatedContent by animateColorAsState(targetContent)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(DuocWhite)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.dllogo),
                contentDescription = "DuocLink Logo",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(250.dp),
                contentScale = ContentScale.Fit

            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Mobile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DuocBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(50.dp))


            Spacer(Modifier.height(12.dp))
            Spacer(Modifier.height(2.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Correo", color = DuocBlue) },
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

            Button(
                onClick = {
                    when {
                        username.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Debe ingresar el correo") }
                        }
                        password.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Debe ingresar la contraseña") }
                        }
                        else -> {
                            isLoading = true
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        navController.navigate(AppRoute.Home.path) {
                                            popUpTo(AppRoute.Login.path) { inclusive = true }
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Error: ${task.exception?.localizedMessage}")
                                        }
                                    }
                                }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = animatedContainer,
                    contentColor = animatedContent
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = animatedContent
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}