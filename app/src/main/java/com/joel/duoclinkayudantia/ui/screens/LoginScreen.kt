package com.joel.duoclinkayudantia.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.joel.duoclinkayudantia.ui.theme.DuocGray
import com.joel.duoclinkayudantia.ui.theme.DuocWhite
import com.joel.duoclinkayudantia.ui.theme.DuocYellow

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Colores objetivo y animados
    val targetContainer = if (isPressed) DuocYellow else DuocBlue
    val targetContent = if (isPressed) DuocBlue else DuocWhite
    val animatedContainer by animateColorAsState(targetContainer)
    val animatedContent by animateColorAsState(targetContent)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuocWhite)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
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

        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario", color = DuocBlue) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DuocBlue,
                unfocusedBorderColor = DuocBlue,
                focusedLabelColor = DuocBlue,
                unfocusedLabelColor = DuocBlue,
                cursorColor = DuocBlue,
                focusedTrailingIconColor = DuocBlue,
                focusedTextColor = DuocBlue,
                unfocusedTextColor = DuocBlue
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = DuocBlue) },
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

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate(AppRoute.Home.path) {
                    // Se elimina la pantalla de Login del back stack para evitar volver atrás a ella.
                    popUpTo(AppRoute.Login.path) { inclusive = true }
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