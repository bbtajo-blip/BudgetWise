package com.budgetwise.expensetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColors = darkColorScheme(
    primary = AccentLime,
    onPrimary = NightDeep,
    secondary = AccentGreen,
    tertiary = AccentPurple,
    background = Night,
    onBackground = TextPrimary,
    surface = Panel,
    onSurface = TextPrimary,
    surfaceVariant = PanelSoft,
    onSurfaceVariant = TextMuted,
    error = Danger,
    onError = Color.White
)

@Composable
fun BudgetWiseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = MaterialTheme.typography,
        content = content
    )
}


