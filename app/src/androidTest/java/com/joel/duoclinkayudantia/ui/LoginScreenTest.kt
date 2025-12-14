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
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

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

        composeTestRule.onNodeWithText("Correo")
            .performTextInput("alumno@duocuc.cl")

        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        composeTestRule.onNodeWithText("Iniciar sesión").assertIsDisplayed().performClick()
    }

    @Test
    fun loginScreen_muestraLogoDuoc() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        composeTestRule.onNodeWithContentDescription("DuocLink Logo").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraErrorSiCorreoVacio() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        composeTestRule.onNodeWithText("Debe ingresar el correo").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraErrorSiPasswordVacio() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Correo").performTextInput("test@duocuc.cl")
        
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        composeTestRule.onNodeWithText("Debe ingresar la contraseña").assertIsDisplayed()
    }
}
