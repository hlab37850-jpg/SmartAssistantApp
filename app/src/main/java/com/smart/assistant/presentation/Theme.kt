package com.smart.assistant.presentation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryDark = Color(0xFF0F172A)
val PrimaryAccent = Color(0xFF4338CA)
val SecondaryAccent = Color(0xFF0EA5E9)
val SurfaceBg = Color(0xFFF8FAFC)
val CardBg = Color(0xFFFFFFFF)
val DebtRed = Color(0xFFEF4444)
val CreditGreen = Color(0xFF10B981)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF1E1B4B),
    secondary = SecondaryAccent,
    background = SurfaceBg,
    surface = CardBg,
    onSurface = Color(0xFF1E293B)
)

@Composable
fun SmartAssistantTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
