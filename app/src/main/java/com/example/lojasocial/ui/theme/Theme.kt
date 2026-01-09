package com.example.lojasocial.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = IpcaGreen,
    onPrimary = Color.White,

    primaryContainer = IpcaGreenDark,
    onPrimaryContainer = Color.White,

    secondary = IpcaGold,
    onSecondary = Color.White,

    secondaryContainer = IpcaGoldDark,
    onSecondaryContainer = Color.White,

    background = IpcaBackgroundLight,
    onBackground = Color(0xFF1B1B1B),

    surface = IpcaSurfaceLight,
    onSurface = Color(0xFF1B1B1B),

    error = IpcaError,
    onError = Color.White,

    outline = Color(0xFFCED4DA)
)

private val DarkColorScheme = darkColorScheme(
    primary = IpcaGreen,
    onPrimary = Color.White,

    primaryContainer = IpcaGreenDark,
    onPrimaryContainer = Color.White,

    secondary = IpcaGold,
    onSecondary = Color.White,

    secondaryContainer = IpcaGoldDark,
    onSecondaryContainer = Color.White,

    background = IpcaBackgroundDark,
    onBackground = Color.White,

    surface = IpcaSurfaceDark,
    onSurface = Color.White,

    error = IpcaError,
    onError = Color.White,

    outline = Color(0xFF2A3A31)
)

@Composable
fun LojaSocialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // recomendo false para manter branding IPCA
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
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