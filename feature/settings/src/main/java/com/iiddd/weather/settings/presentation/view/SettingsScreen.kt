package com.iiddd.weather.settings.presentation.view

import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    SettingsScreenContent(
        title = "Settings",
        onNavigateBack = onNavigateBack
    )
}