package com.joel.duoclinkayudantia.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DuocBlue,
    secondary = DuocYellow,
    tertiary = DuocYellow,
    background = Color(0xFF0A0A0A),
    surface = DuocBlue,
    onPrimary = Color.White,
    onSecondary = DuocBlue,
    onTertiary = DuocBlue,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = DuocBlue,
    secondary = DuocYellow,
    tertiary = DuocYellow,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = DuocBlue,
    onTertiary = DuocBlue,
    onBackground = DuocBlue,
    onSurface = DuocBlue
)

@Composable
fun DuocLinkAyudantiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}