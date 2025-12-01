package com.joel.duoclinkayudantia.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.joel.duoclinkayudantia.ui.screens.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_debeMostrarCamposYBoton() {
        // 1. Renderizar la pantalla
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        // 2. Verificar que existen los textos clave
        composeTestRule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun loginScreen_permiteEscribirCredenciales() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        // 3. Simular escritura de usuario
        // Buscamos el campo por su etiqueta (Label) y escribimos
        composeTestRule.onNodeWithText("Correo")
            .performTextInput("alumno@duocuc.cl")

        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        // 4. Verificar que el botón existe y hacer click
        composeTestRule.onNodeWithText("Iniciar sesión").assertIsDisplayed().performClick()
    }

    @Test
    fun loginScreen_muestraLogoDuoc() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        // Verificar que la imagen con la descripción de contenido existe
        composeTestRule.onNodeWithContentDescription("DuocLink Logo").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraErrorSiCorreoVacio() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        // Click en iniciar sesión sin escribir nada
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        // Verificar que aparece el mensaje de error (Snackbar)
        // Nota: Puede requerir esperar un poco o buscar en el árbol semántico
        composeTestRule.onNodeWithText("Debe ingresar el correo").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraErrorSiPasswordVacio() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        // Escribir correo pero no contraseña
        composeTestRule.onNodeWithText("Correo").performTextInput("test@duocuc.cl")
        
        // Click en iniciar sesión
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        // Verificar mensaje de error
        composeTestRule.onNodeWithText("Debe ingresar la contraseña").assertIsDisplayed()
    }
}
