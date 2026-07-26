package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SmartPlusColorScheme = darkColorScheme(
    primary = CyberGold,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3B2C00),
    onPrimaryContainer = CyberGold,
    secondary = CyberCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF003840),
    onSecondaryContainer = CyberCyan,
    tertiary = CyberCrimson,
    onTertiary = Color.White,
    background = SlateDarkBackground,
    onBackground = TextPrimary,
    surface = SlateCardSurface,
    onSurface = TextPrimary,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = SlateBorder
)

@Composable
fun SmartPlusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SmartPlusColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SmartPlusTheme(content = content)
}
