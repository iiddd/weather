package com.iiddd.weather.settings.presentation.view

import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    // If a SettingsViewModel is added later, state handling and side effects should live here.
    SettingsScreenContent(
        title = "Settings",
        onNavigateBack = onNavigateBack
    )
}