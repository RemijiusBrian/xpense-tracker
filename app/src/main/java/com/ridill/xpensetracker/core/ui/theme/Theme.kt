package com.ridill.xpensetracker.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DarkPrimary,
    primaryVariant = DarkPrimaryContainer,
    secondary = DarkSecondary,
    secondaryVariant = DarkSecondaryContainer,
    background = DarkBackground,
    surface = DarkSurface,
    error = Error,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onError = DarkOnError
)

private val LightColorPalette = lightColors(
    primary = LightPrimary,
    primaryVariant = LightPrimaryContainer,
    secondary = LightSecondary,
    secondaryVariant = LightSecondaryContainer,
    background = LightBackground,
    surface = LightSurface,
    error = LightError,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onError = LightOnError
)

@Composable
fun XpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    val systemBarsColor = if (darkTheme) DarkSurfaceVariant else MoneyGreen
    SideEffect {
        systemUiController.setSystemBarsColor(color = systemBarsColor)
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}