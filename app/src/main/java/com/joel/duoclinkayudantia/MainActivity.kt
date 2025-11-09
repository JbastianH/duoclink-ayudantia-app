package com.joel.duoclinkayudantia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.joel.duoclinkayudantia.navigation.AppNavigation
import com.joel.duoclinkayudantia.ui.theme.DuocLinkAyudantiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuocLinkAyudantiaTheme {
                AppNavigation()
            }
        }
    }
}